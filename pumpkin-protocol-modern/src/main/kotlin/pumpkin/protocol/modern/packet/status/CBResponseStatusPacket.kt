package pumpkin.protocol.modern.packet.status

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler
import pumpkin.protocol.modern.readVarIntString
import pumpkin.protocol.modern.type.PingResponse
import pumpkin.protocol.modern.writeVarIntString
import pumpkin.util.serialization.Formats

data class CBResponseStatusPacket(val response: PingResponse = PingResponse.DEFAULT) : ModernPacket.Status() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBResponseStatusPacket> {
        override fun read(buf: ByteBuf): CBResponseStatusPacket =
            CBResponseStatusPacket(
                response = Formats.jsonNoDefaults.parse(PingResponse.serializer(), buf.readVarIntString())
            )

        override fun write(buf: ByteBuf, packet: CBResponseStatusPacket) {
            buf.writeVarIntString(Formats.jsonNoDefaults.stringify(PingResponse.serializer(), packet.response))
        }
    }
}