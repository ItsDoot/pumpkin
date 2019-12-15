package pumpkin.protocol.modern.packet.login

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.*
import pumpkin.text.Text

data class CBDisconnectLoginPacket(
    val reason: Text
) : ModernPacket.Login() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBDisconnectLoginPacket> {
        override fun read(buf: ByteBuf): CBDisconnectLoginPacket =
            CBDisconnectLoginPacket(
                reason = Text.fromJsonString(buf.readVarIntString())
            )

        override fun write(buf: ByteBuf, packet: CBDisconnectLoginPacket) {
            buf.writeVarIntString(packet.reason.toJsonString())
        }
    }
}