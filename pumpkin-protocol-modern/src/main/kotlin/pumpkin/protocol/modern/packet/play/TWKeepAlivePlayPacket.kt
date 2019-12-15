package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler

data class TWKeepAlivePlayPacket(
    val keepAlive: Long
): ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<TWKeepAlivePlayPacket> {
        override fun read(buf: ByteBuf): TWKeepAlivePlayPacket =
            TWKeepAlivePlayPacket(
                keepAlive = buf.readLong()
            )

        override fun write(buf: ByteBuf, packet: TWKeepAlivePlayPacket) {
            buf.writeLong(packet.keepAlive)
        }
    }
}