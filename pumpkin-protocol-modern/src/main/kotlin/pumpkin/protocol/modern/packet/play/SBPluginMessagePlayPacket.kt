package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import io.vertx.core.buffer.Buffer
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.type.Identifier
import pumpkin.protocol.modern.type.readIdentifier
import pumpkin.protocol.modern.type.writeIdentifier

data class SBPluginMessagePlayPacket(
    val channel: Identifier,
    val data: ByteArray
) : ModernPacket.Play() {

    val brand: String? =
        if (channel.toString() == "minecraft:brand")
            Buffer.buffer(data).byteBuf.readVarIntString()
        else null

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<SBPluginMessagePlayPacket> {
        override fun read(buf: ByteBuf): SBPluginMessagePlayPacket =
            SBPluginMessagePlayPacket(
                channel = buf.readIdentifier(),
                data = buf.readRemainingBytes()
            )

        override fun write(buf: ByteBuf, packet: SBPluginMessagePlayPacket) {
            buf.writeIdentifier(packet.channel)
                .writeBytes(packet.data)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SBPluginMessagePlayPacket) return false

        if (channel != other.channel) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = channel.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}