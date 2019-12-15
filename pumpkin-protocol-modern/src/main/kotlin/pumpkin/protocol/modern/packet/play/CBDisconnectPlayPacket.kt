package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec
import pumpkin.text.Text

data class CBDisconnectPlayPacket(
    val reason: Text
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBDisconnectPlayPacket> {
        override fun read(buf: ByteBuf): CBDisconnectPlayPacket =
            CBDisconnectPlayPacket(
                reason = Text.fromJsonString(buf.readVarIntString())
            )

        override fun write(buf: ByteBuf, packet: CBDisconnectPlayPacket) {
            buf.writeVarIntString(packet.reason.toJsonString())
        }
    }
}