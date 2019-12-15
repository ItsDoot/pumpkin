package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.type.GameMode
import pumpkin.protocol.modern.type.readGameMode
import pumpkin.protocol.modern.type.writeGameMode
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.type.*
import pumpkin.text.Text
import java.util.*

data class CBPlayerInfoPlayPacket(
    val action: Int,
    val playerInfos: List<PlayerInfo>
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    sealed class PlayerInfo {
        abstract val uuid: UUID

        data class Add(
            override val uuid: UUID,
            val properties: List<PlayerProperty>,
            val gamemode: GameMode,
            val ping: Int,
            val displayName: Text?
        ) : PlayerInfo()

        data class UpdateGameMode(
            override val uuid: UUID,
            val gamemode: GameMode
        ) : PlayerInfo()

        data class UpdateLatency(
            override val uuid: UUID,
            val ping: Int
        ) : PlayerInfo()

        data class UpdateDisplayName(
            override val uuid: UUID,
            val displayName: Text?
        ) : PlayerInfo()

        data class Remove(
            override val uuid: UUID
        ) : PlayerInfo()
    }

    companion object : PacketCodec<CBPlayerInfoPlayPacket> {
        val ADD_EMPTY = CBPlayerInfoPlayPacket(0, emptyList())
        val UPDATE_LATENCY_EMPTY = CBPlayerInfoPlayPacket(2, emptyList())

        override fun read(buf: ByteBuf): CBPlayerInfoPlayPacket {
            val action = buf.readVarInt()
            val playerInfos = buf.readVarIntList {
                val uuid = buf.readUUID()
                when (action) {
                    0 -> PlayerInfo.Add(
                        uuid = uuid,
                        properties = buf.readVarIntList(ByteBuf::readPlayerProperty),
                        gamemode = buf.readGameMode(),
                        ping = buf.readVarInt(),
                        displayName = if (buf.readBoolean()) buf.readText() else null
                    )
                    1 -> PlayerInfo.UpdateGameMode(
                        uuid = uuid,
                        gamemode = buf.readGameMode()
                    )
                    2 -> PlayerInfo.UpdateLatency(
                        uuid = uuid,
                        ping = buf.readVarInt()
                    )
                    3 -> PlayerInfo.UpdateDisplayName(
                        uuid = uuid,
                        displayName = if (buf.readBoolean()) buf.readText() else null
                    )
                    4 -> PlayerInfo.Remove(
                        uuid = uuid
                    )
                    else -> throw IllegalStateException("Unknown player info action type: $action")
                }
            }
            return CBPlayerInfoPlayPacket(action, playerInfos)
        }

        override fun write(buf: ByteBuf, packet: CBPlayerInfoPlayPacket) {
            buf.writeVarInt(packet.action)
                .writeVarIntList(packet.playerInfos) {
                    this.writeUUID(it.uuid)
                    when (it) {
                        is PlayerInfo.Add -> {
                            check(packet.action == 0) { "Invalid action type (is 0, but requires ${packet.action}" }

                            this.writeVarIntList(it.properties, ByteBuf::writePlayerProperty)
                                .writeGameMode(it.gamemode)
                                .writeVarInt(it.ping)
                                .writeBoolean(it.displayName != null)
                            if (it.displayName != null)
                                this.writeText(it.displayName)
                        }
                        is PlayerInfo.UpdateGameMode -> {
                            check(packet.action == 1) { "Invalid action type (is 1, but requires ${packet.action}" }

                            this.writeGameMode(it.gamemode)
                        }
                        is PlayerInfo.UpdateLatency -> {
                            check(packet.action == 2) { "Invalid action type (is 2, but requires ${packet.action}" }

                            this.writeVarInt(it.ping)
                        }
                        is PlayerInfo.UpdateDisplayName -> {
                            check(packet.action == 3) { "Invalid action type (is 3, but requires ${packet.action}" }

                            this.writeBoolean(it.displayName != null)
                            if (it.displayName != null)
                                this.writeText(it.displayName)
                        }
                        is PlayerInfo.Remove -> {
                            check(packet.action == 4) { "Invalid action type (is 4, but requires ${packet.action}" }
                        }
                    }
                    this
                }
        }
    }
}