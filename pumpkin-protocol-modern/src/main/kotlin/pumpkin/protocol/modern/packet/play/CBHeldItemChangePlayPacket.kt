package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler

data class CBHeldItemChangePlayPacket(val slot: Int) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBHeldItemChangePlayPacket> {
        val INITIAL = CBHeldItemChangePlayPacket(0)

        override fun read(buf: ByteBuf): CBHeldItemChangePlayPacket =
            CBHeldItemChangePlayPacket(buf.readByte().toInt())

        override fun write(buf: ByteBuf, packet: CBHeldItemChangePlayPacket) {
            buf.writeByte(packet.slot)
        }
    }
}