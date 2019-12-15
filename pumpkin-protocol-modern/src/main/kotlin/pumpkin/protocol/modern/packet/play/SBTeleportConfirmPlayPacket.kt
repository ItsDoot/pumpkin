package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec

data class SBTeleportConfirmPlayPacket(
    val teleportId: Int
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<SBTeleportConfirmPlayPacket> {
        override fun read(buf: ByteBuf): SBTeleportConfirmPlayPacket =
            SBTeleportConfirmPlayPacket(
                teleportId = buf.readVarInt()
            )

        override fun write(buf: ByteBuf, packet: SBTeleportConfirmPlayPacket) {
            buf.writeVarInt(packet.teleportId)
        }
    }
}