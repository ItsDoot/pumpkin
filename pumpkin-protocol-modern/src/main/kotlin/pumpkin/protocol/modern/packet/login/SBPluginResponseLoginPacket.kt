package pumpkin.protocol.modern.packet.login

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.*

data class SBPluginResponseLoginPacket(
    val messageId: Int,
    val successful: Boolean,
    val data: ByteArray
) : ModernPacket.Login() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SBPluginResponseLoginPacket) return false

        if (messageId != other.messageId) return false
        if (successful != other.successful) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = messageId
        result = 31 * result + successful.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }

    companion object : PacketCodec<SBPluginResponseLoginPacket> {
        override fun read(buf: ByteBuf): SBPluginResponseLoginPacket =
            SBPluginResponseLoginPacket(
                messageId = buf.readVarInt(),
                successful = buf.readBoolean(),
                data = buf.readRemainingBytes()
            )

        override fun write(buf: ByteBuf, packet: SBPluginResponseLoginPacket) {
            buf.writeVarInt(packet.messageId)
                .writeBoolean(packet.successful)
                .writeBytes(packet.data)
        }
    }
}