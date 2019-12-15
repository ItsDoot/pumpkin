package pumpkin.protocol.modern.packet.login

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.*
import java.util.*

data class CBSuccessLoginPacket(
    val uuid: UUID,
    val username: String
) : ModernPacket.Login() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBSuccessLoginPacket> {
        override fun read(buf: ByteBuf): CBSuccessLoginPacket =
            CBSuccessLoginPacket(
                uuid = UUID.fromString(buf.readVarIntString(36)),
                username = buf.readVarIntString(16)
            )

        override fun write(buf: ByteBuf, packet: CBSuccessLoginPacket) {
            buf.writeVarIntString(packet.uuid.toString())
                .writeVarIntString(packet.username)
        }
    }
}