package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.type.ChatType
import pumpkin.protocol.modern.type.readChatType
import pumpkin.protocol.modern.type.readText
import pumpkin.protocol.modern.type.writeChatType
import pumpkin.protocol.modern.type.writeText
import pumpkin.text.Text

data class CBChatMessagePlayPacket(
    val text: Text,
    val chatType: ChatType
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBChatMessagePlayPacket> {
        override fun read(buf: ByteBuf): CBChatMessagePlayPacket =
            CBChatMessagePlayPacket(
                text = buf.readText(),
                chatType = buf.readChatType()
            )

        override fun write(buf: ByteBuf, packet: CBChatMessagePlayPacket) {
            buf.writeText(packet.text)
                .writeChatType(packet.chatType)
        }
    }
}