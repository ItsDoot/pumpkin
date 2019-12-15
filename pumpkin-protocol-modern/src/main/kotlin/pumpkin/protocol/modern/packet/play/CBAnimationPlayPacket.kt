package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec

data class CBAnimationPlayPacket(
    val entityId: Int,
    val animation: Int
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBAnimationPlayPacket> {
        override fun read(buf: ByteBuf): CBAnimationPlayPacket =
            CBAnimationPlayPacket(
                entityId = buf.readVarInt(),
                animation = buf.readUnsignedByte().toInt()
            )

        override fun write(buf: ByteBuf, packet: CBAnimationPlayPacket) {
            buf.writeVarInt(packet.entityId)
                .writeByte(packet.animation)
        }
    }
}