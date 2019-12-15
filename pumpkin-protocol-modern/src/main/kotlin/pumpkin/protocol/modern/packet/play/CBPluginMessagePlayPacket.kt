package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import io.vertx.core.buffer.Buffer
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.type.Identifier
import pumpkin.protocol.modern.type.readIdentifier
import pumpkin.protocol.modern.type.writeIdentifier

data class CBPluginMessagePlayPacket(
    val channel: Identifier,
    val data: ByteArray
) : ModernPacket.Play() {
    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBPluginMessagePlayPacket> {
        fun brand(brand: String): CBPluginMessagePlayPacket =
            CBPluginMessagePlayPacket(
                Identifier("minecraft:brand"),
                Buffer.buffer().byteBuf.writeVarIntString(brand).readRemainingBytes()
            )

        override fun read(buf: ByteBuf): CBPluginMessagePlayPacket =
            CBPluginMessagePlayPacket(
                channel = buf.readIdentifier(),
                data = buf.readRemainingBytes()
            )

        override fun write(buf: ByteBuf, packet: CBPluginMessagePlayPacket) {
            buf.writeIdentifier(packet.channel)
                .writeBytes(packet.data)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CBPluginMessagePlayPacket) return false

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