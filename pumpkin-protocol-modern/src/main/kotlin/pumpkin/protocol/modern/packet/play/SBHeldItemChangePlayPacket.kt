package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler

data class SBHeldItemChangePlayPacket(
    val slot: Int
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<SBHeldItemChangePlayPacket> {
        override fun read(buf: ByteBuf): SBHeldItemChangePlayPacket =
            SBHeldItemChangePlayPacket(
                slot = buf.readShort().toInt()
            )

        override fun write(buf: ByteBuf, packet: SBHeldItemChangePlayPacket) {
            buf.writeShort(packet.slot)
        }
    }
}