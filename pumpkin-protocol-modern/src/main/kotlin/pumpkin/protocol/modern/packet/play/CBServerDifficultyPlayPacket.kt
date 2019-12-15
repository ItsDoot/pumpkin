package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler
import pumpkin.protocol.modern.type.Difficulty

data class CBServerDifficultyPlayPacket(
    val difficulty: Difficulty,
    val locked: Boolean
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBServerDifficultyPlayPacket> {
        override fun read(buf: ByteBuf): CBServerDifficultyPlayPacket =
            CBServerDifficultyPlayPacket(
                difficulty = Difficulty[buf.readUnsignedByte().toInt()],
                locked = if (buf.readableBytes() > 0) buf.readBoolean() else false
            )

        override fun write(buf: ByteBuf, packet: CBServerDifficultyPlayPacket) {
            buf.writeByte(packet.difficulty.id)
                .writeBoolean(packet.locked)
        }
    }
}