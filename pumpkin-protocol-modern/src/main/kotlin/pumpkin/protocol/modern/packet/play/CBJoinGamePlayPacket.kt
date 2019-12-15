package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.type.GameMode
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.type.DimensionType

data class CBJoinGamePlayPacket(
    val entityId: Int,
    val gamemode: GameMode,
    val dimension: DimensionType,
    val maxPlayers: Int,
    val levelType: String,
    val viewDistance: Int,
    val reducedDebugInfo: Boolean
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBJoinGamePlayPacket> {
        override fun read(buf: ByteBuf): CBJoinGamePlayPacket =
            CBJoinGamePlayPacket(
                entityId = buf.readInt(),
                gamemode = buf.readUnsignedByte().toInt().let { requireNotNull(GameMode.FROM_ID[it]) { "Unknown gamemode: $it" } },
                dimension = buf.readInt().let { requireNotNull(DimensionType.FROM_ID[it]) { "Unknown dimension type: $it" } },
                maxPlayers = buf.readUnsignedByte().toInt(),
                levelType = buf.readVarIntString(16),
                viewDistance = buf.readVarInt(),
                reducedDebugInfo = buf.readBoolean()
            )

        override fun write(buf: ByteBuf, packet: CBJoinGamePlayPacket) {
            buf.writeInt(packet.entityId)
                .writeByte(packet.gamemode.id)
                .writeInt(packet.dimension.id)
                .writeByte(packet.maxPlayers)
                .writeVarIntString(packet.levelType)
                .writeVarInt(packet.viewDistance)
                .writeBoolean(packet.reducedDebugInfo)
        }
    }
}