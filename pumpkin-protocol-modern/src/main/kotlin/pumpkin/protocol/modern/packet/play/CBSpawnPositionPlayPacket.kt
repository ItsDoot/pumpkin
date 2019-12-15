package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.type.Position
import pumpkin.protocol.modern.type.readPosition
import pumpkin.protocol.modern.type.writePosition

data class CBSpawnPositionPlayPacket(
    val location: Position
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBSpawnPositionPlayPacket> {
        val ZERO = CBSpawnPositionPlayPacket(Position(0, 0, 0))

        override fun read(buf: ByteBuf): CBSpawnPositionPlayPacket =
            CBSpawnPositionPlayPacket(
                location = buf.readPosition()
            )

        override fun write(buf: ByteBuf, packet: CBSpawnPositionPlayPacket) {
            buf.writePosition(packet.location)
        }
    }
}