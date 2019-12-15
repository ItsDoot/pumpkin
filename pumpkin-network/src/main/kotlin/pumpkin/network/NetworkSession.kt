package pumpkin.network

import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import mu.KLogger
import mu.KotlinLogging
import pumpkin.auth.GameProfile
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler
import pumpkin.protocol.modern.ModernProtocolState
import pumpkin.protocol.modern.ModernProtocolVersion
import pumpkin.protocol.modern.packet.handshake.SBHandshakePacket
import pumpkin.protocol.modern.packet.login.CBDisconnectLoginPacket
import pumpkin.protocol.modern.packet.login.CBSuccessLoginPacket
import pumpkin.protocol.modern.packet.login.SBStartLoginPacket
import pumpkin.protocol.modern.packet.play.*
import pumpkin.protocol.modern.packet.status.CBResponseStatusPacket
import pumpkin.protocol.modern.packet.status.SBRequestStatusPacket
import pumpkin.protocol.modern.packet.status.TWPingPongStatusPacket
import pumpkin.protocol.modern.readVarInt
import pumpkin.protocol.modern.sizeWrapVarInt
import pumpkin.protocol.modern.type.ChatType
import pumpkin.protocol.modern.type.Difficulty
import pumpkin.protocol.modern.type.DimensionType
import pumpkin.protocol.modern.type.GameMode
import pumpkin.protocol.modern.writeVarInt
import pumpkin.text.LiteralText
import pumpkin.text.Text
import pumpkin.util.UUIDs
import pumpkin.util.logger

data class NetworkSession(
    val vertx: Vertx,
    val server: NetworkServer,
    val socket: NetSocket
) : ModernPacketHandler {
    companion object {
        @JvmField
        val LOGGER: KLogger = KotlinLogging.logger<NetworkSession>()
    }

    /**
     * The game version this client has connected with.
     */
    private var version: ModernProtocolVersion = ModernProtocolVersion.MINIMUM

    /**
     * The currently active protocol state.
     */
    private var curState: ModernProtocolState = ModernProtocolState.HANDSHAKE

    /**
     * The user profile that uniquely identifies this player.
     */
    private var profile: GameProfile? = null

    /**
     * The current keep alive time in milliseconds.
     * Is -1 whenever a keep alive has been acknowledged.
     */
    private var keepAlive: Long = -1L

    /**
     * The timer id of the keep alive task.
     * Is -1 whenever keep alive hasn't started yet.
     */
    private var keepAliveTimer: Long = -1L

    /**
     * Whether this connection is open.
     */
    private var connected: Boolean = true

    /**
     * The reason that the client disconnected.
     */
    private var disconnectReason: Text? = null

    internal fun init() {

    }

    internal fun close() {
        this.connected = false
        if (this.keepAliveTimer != -1L) {
            this.vertx.cancelTimer(this.keepAliveTimer)
        }

        LOGGER.info("${this.profile?.username ?: this.socket.remoteAddress()} disconnected from the server.")
    }

    ////////////////////////////////////////////////////
    // Keep-Alive

    private fun initKeepAlive() {
        if (this.keepAliveTimer != -1L) {
            this.vertx.cancelTimer(this.keepAliveTimer)
        }
        this.idle()
        this.keepAliveTimer = this.vertx.setPeriodic(15000) {
            this.idle()
        }
    }

    private fun idle() {
        if (this.curState == ModernProtocolState.PLAY) {
            val time = System.currentTimeMillis()
            if (this.keepAlive == -1L) {
                this.keepAlive = time
                this.write(TWKeepAlivePlayPacket(time))
            } else {
                this.disconnect(LiteralText("Timeout error."))
            }
        }
    }

    override fun handle(packet: TWKeepAlivePlayPacket) {
        if (this.keepAlive == packet.keepAlive) {
            this.keepAlive = -1L
        }
    }

    ////////////////////////////////////////////////////
    // Packet I/O

    private val parser: PacketParser = PacketParser {
        try {
            val id = it.readVarInt()
            val codec = this.curState.serverbound[this.version][id]
            val packet = codec.read(it)
            LOGGER.info("${socket.remoteAddress()} IN - $packet")
            packet.handle(this)
        } catch (e: Exception) {
            e.printStackTrace()
            this.disconnect(LiteralText(e.message ?: "An error occurred while handling a packet."))
        }
    }

    internal fun receive(event: Buffer) {
        this.parser.handle(event)
    }

    fun write(packet: ModernPacket) {
        LOGGER.info("${socket.remoteAddress()} OUT - $packet")

        val buffer = Buffer.buffer().byteBuf
        val id = this.curState.clientbound[this.version].getId(packet::class)
        val codec = this.curState.clientbound[this.version][id]

        buffer.writeVarInt(id)
        codec.write(buffer, packet)

        this.socket.write(Buffer.buffer(buffer.sizeWrapVarInt()))
    }

    private fun disconnect(reason: Text) {
        this.disconnectReason = reason
        when (this.curState) {
            ModernProtocolState.PLAY -> {
                this.write(CBDisconnectPlayPacket(reason))
            }
            ModernProtocolState.LOGIN -> {
                this.write(CBDisconnectLoginPacket(reason))
            }
            else -> {
                // No packet to send in other states; just close the socket.
            }
        }
        this.socket.close()
        this.connected = false
    }

    private fun sendMessage(message: Text, chatType: ChatType = ChatType.SYSTEM) {
        this.write(CBChatMessagePlayPacket(message, chatType))
    }

    ////////////////////////////////////////////////////
    // Packet Handlers

    override fun handle(packet: SBHandshakePacket) {
        this.version = ModernProtocolVersion[packet.protocolVersion]
        LOGGER.info("Received ${this.version} connection from ${this.socket.remoteAddress()}")

        if (this.version == ModernProtocolVersion.Unknown) {
            // Close the connection for unknown client versions.
            this.socket.close()
        }

        this.curState = ModernProtocolState.fromHandshake(packet.nextState)
    }

    override fun handle(packet: SBStartLoginPacket) {
        // Successful login; switch to play mode.
        val profile = GameProfile(UUIDs.offline(packet.username), packet.username)
        this.profile = profile

        this.write(CBSuccessLoginPacket(profile.uuid, profile.username))
        this.curState = ModernProtocolState.PLAY

        // Initialize keep alive
        initKeepAlive()

        // Send initial loading packets.
        this.write(
            CBJoinGamePlayPacket(
                entityId = 0,
                gamemode = GameMode.SURVIVAL,
                dimension = DimensionType.OVERWORLD,
                maxPlayers = 100,
                levelType = "world",
                viewDistance = 10,
                reducedDebugInfo = false
            )
        )
        this.write(CBPluginMessagePlayPacket.brand("pumpkin"))
        this.write(CBServerDifficultyPlayPacket(Difficulty.EASY, locked = true))
        this.write(
            CBPlayerAbilitiesPlayPacket(
                invulnerable = false,
                flying = true,
                canFly = true,
                instantBreak = false,
                flySpeed = 0.05F,
                fovModifier = 0.1F
            )
        )
    }

    override fun handle(packet: SBChatMessagePlayPacket) {

    }

    override fun handle(packet: SBClientSettingsPlayPacket) {
        this.write(CBHeldItemChangePlayPacket.INITIAL)
        // TODO: declare recipes
        this.write(CBTagsPlayPacket.EMPTY)
        // TODO: declare commands
        // TODO: unlock recipes
        this.write(CBPlayerPositionAndLookPlayPacket.ZERO)
        this.write(CBPlayerInfoPlayPacket.ADD_EMPTY)
        this.write(CBPlayerInfoPlayPacket.UPDATE_LATENCY_EMPTY)
        this.write(CBUpdateViewPositionPlayPacket.ZERO)
        this.write(CBSpawnPositionPlayPacket.ZERO)
        this.write(CBPlayerPositionAndLookPlayPacket.ZERO)
    }

    override fun handle(packet: SBRequestStatusPacket) {
        this.write(CBResponseStatusPacket())
    }

    override fun handle(packet: TWPingPongStatusPacket) {
        this.write(packet)
    }
}