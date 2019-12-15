package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler
import pumpkin.protocol.modern.type.Difficulty
import pumpkin.protocol.modern.type.readDifficulty
import pumpkin.protocol.modern.type.writeDifficulty

data class SBSetDifficultyPlayPacket(
    val newDifficulty: Difficulty
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<SBSetDifficultyPlayPacket> {
        override fun read(buf: ByteBuf): SBSetDifficultyPlayPacket =
            SBSetDifficultyPlayPacket(
                buf.readDifficulty()
            )

        override fun write(buf: ByteBuf, packet: SBSetDifficultyPlayPacket) {
            buf.writeDifficulty(packet.newDifficulty)
        }
    }
}