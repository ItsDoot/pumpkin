package pumpkin.protocol.modern.packet.status

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler

data class TWPingPongStatusPacket(val payload: Long) : ModernPacket.Status() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<TWPingPongStatusPacket> {
        override fun read(buf: ByteBuf): TWPingPongStatusPacket =
            TWPingPongStatusPacket(
                payload = buf.readLong()
            )

        override fun write(buf: ByteBuf, packet: TWPingPongStatusPacket) {
            buf.writeLong(packet.payload)
        }
    }
}