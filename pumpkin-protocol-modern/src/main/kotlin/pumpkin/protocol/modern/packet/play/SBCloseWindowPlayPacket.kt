package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler

data class SBCloseWindowPlayPacket(val windowId: Int) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<SBCloseWindowPlayPacket> {
        override fun read(buf: ByteBuf): SBCloseWindowPlayPacket =
            SBCloseWindowPlayPacket(buf.readUnsignedByte().toInt())

        override fun write(buf: ByteBuf, packet: SBCloseWindowPlayPacket) {
            buf.writeByte(packet.windowId)
        }
    }
}