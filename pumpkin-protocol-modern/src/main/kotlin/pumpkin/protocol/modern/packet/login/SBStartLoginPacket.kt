package pumpkin.protocol.modern.packet.login

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.*

data class SBStartLoginPacket(
    val username: String
) : ModernPacket.Login() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<SBStartLoginPacket> {
        override fun read(buf: ByteBuf): SBStartLoginPacket =
            SBStartLoginPacket(
                username = buf.readVarIntString(16)
            )

        override fun write(buf: ByteBuf, packet: SBStartLoginPacket) {
            buf.writeVarIntString(packet.username)
        }
    }
}