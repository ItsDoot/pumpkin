package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec

data class SBChatMessagePlayPacket(
    val message: String
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<SBChatMessagePlayPacket> {
        override fun read(buf: ByteBuf): SBChatMessagePlayPacket =
            SBChatMessagePlayPacket(
                message = buf.readVarIntString(256)
            )

        override fun write(buf: ByteBuf, packet: SBChatMessagePlayPacket) {
            buf.writeVarIntString(packet.message)
        }
    }
}