package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.type.Position
import pumpkin.protocol.modern.type.readPosition
import pumpkin.protocol.modern.type.writePosition

data class CBBlockActionPlayPacket(
    val location: Position,
    val actionId: Int,
    val actionParam: Int,
    val blockType: Int
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBBlockActionPlayPacket> {
        override fun read(buf: ByteBuf): CBBlockActionPlayPacket =
            CBBlockActionPlayPacket(
                location = buf.readPosition(),
                actionId = buf.readUnsignedByte().toInt(),
                actionParam = buf.readUnsignedByte().toInt(),
                blockType = buf.readVarInt()
            )

        override fun write(buf: ByteBuf, packet: CBBlockActionPlayPacket) {
            buf.writePosition(packet.location)
                .writeByte(packet.actionId)
                .writeByte(packet.actionParam)
                .writeVarInt(packet.blockType)
        }
    }
}