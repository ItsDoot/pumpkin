package pumpkin.protocol.modern.packet.login

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.*

data class CBPluginRequestLoginPacket(
    val messageId: Int,
    val channel: String,
    val data: ByteArray
) : ModernPacket.Login() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CBPluginRequestLoginPacket) return false

        if (messageId != other.messageId) return false
        if (channel != other.channel) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = messageId
        result = 31 * result + channel.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }

    companion object : PacketCodec<CBPluginRequestLoginPacket> {
        override fun read(buf: ByteBuf): CBPluginRequestLoginPacket =
            CBPluginRequestLoginPacket(
                messageId = buf.readVarInt(),
                channel = buf.readVarIntString(),
                data = buf.readRemainingBytes()
            )

        override fun write(buf: ByteBuf, packet: CBPluginRequestLoginPacket) {
            buf.writeVarInt(packet.messageId)
                .writeVarIntString(packet.channel)
                .writeBytes(packet.data)
        }
    }
}