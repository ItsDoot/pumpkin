package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler
import pumpkin.protocol.modern.type.DigStatus
import pumpkin.protocol.modern.type.Face
import pumpkin.protocol.modern.type.Position
import pumpkin.protocol.modern.type.readDigStatus
import pumpkin.protocol.modern.type.readFace
import pumpkin.protocol.modern.type.readPosition
import pumpkin.protocol.modern.type.writeDigStatus
import pumpkin.protocol.modern.type.writeFace
import pumpkin.protocol.modern.type.writePosition

data class SBPlayerDiggingPlayPacket(
    val status: DigStatus,
    val location: Position,
    val face: Face
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<SBPlayerDiggingPlayPacket> {
        override fun read(buf: ByteBuf): SBPlayerDiggingPlayPacket =
            SBPlayerDiggingPlayPacket(buf.readDigStatus(), buf.readPosition(), buf.readFace())

        override fun write(buf: ByteBuf, packet: SBPlayerDiggingPlayPacket) {
            buf.writeDigStatus(packet.status)
                .writePosition(packet.location)
                .writeFace(packet.face)
        }
    }
}