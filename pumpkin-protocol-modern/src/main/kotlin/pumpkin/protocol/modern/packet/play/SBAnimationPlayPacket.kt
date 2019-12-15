package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.type.HandType
import pumpkin.protocol.modern.type.readHandType
import pumpkin.protocol.modern.type.writeHandType

data class SBAnimationPlayPacket(
    val hand: HandType
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<SBAnimationPlayPacket> {
        override fun read(buf: ByteBuf): SBAnimationPlayPacket =
            SBAnimationPlayPacket(
                hand = buf.readHandType()
            )

        override fun write(buf: ByteBuf, packet: SBAnimationPlayPacket) {
            buf.writeHandType(packet.hand)
        }
    }
}