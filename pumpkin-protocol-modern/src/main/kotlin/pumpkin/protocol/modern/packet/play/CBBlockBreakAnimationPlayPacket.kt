package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.type.Position
import pumpkin.protocol.modern.type.readPosition
import pumpkin.protocol.modern.type.writePosition

data class CBBlockBreakAnimationPlayPacket(
    val entityId: Int,
    val location: Position,
    val destroyStage: Int
): ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBBlockBreakAnimationPlayPacket> {
        override fun read(buf: ByteBuf): CBBlockBreakAnimationPlayPacket =
            CBBlockBreakAnimationPlayPacket(
                entityId = buf.readVarInt(),
                location = buf.readPosition(),
                destroyStage = buf.readByte().toInt()
            )

        override fun write(buf: ByteBuf, packet: CBBlockBreakAnimationPlayPacket) {
            buf.writeVarInt(packet.entityId)
                .writePosition(packet.location)
                .writeByte(packet.destroyStage)
        }
    }
}