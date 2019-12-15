package pumpkin.protocol.modern.packet.handshake

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler
import pumpkin.protocol.modern.readVarInt
import pumpkin.protocol.modern.readVarIntString
import pumpkin.protocol.modern.writeVarInt
import pumpkin.protocol.modern.writeVarIntString

data class SBHandshakePacket(
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: Int,
    val nextState: Int
) : ModernPacket.Handshake() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<SBHandshakePacket> {
        override fun read(buf: ByteBuf): SBHandshakePacket =
            SBHandshakePacket(
                protocolVersion = buf.readVarInt(),
                serverAddress = buf.readVarIntString(),
                serverPort = buf.readUnsignedShort(),
                nextState = buf.readVarInt()
            )

        override fun write(buf: ByteBuf, packet: SBHandshakePacket) {
            buf.writeVarInt(packet.protocolVersion)
                .writeVarIntString(packet.serverAddress)
                .writeShort(packet.serverPort)
                .writeVarInt(packet.nextState)
        }
    }
}