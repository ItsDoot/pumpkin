package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec

data class CBUpdateViewPositionPlayPacket(
    val chunkX: Int,
    val chunkZ: Int
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBUpdateViewPositionPlayPacket> {
        val ZERO = CBUpdateViewPositionPlayPacket(chunkX = 0, chunkZ = 0)

        override fun read(buf: ByteBuf): CBUpdateViewPositionPlayPacket =
            CBUpdateViewPositionPlayPacket(
                chunkX = buf.readVarInt(),
                chunkZ = buf.readVarInt()
            )

        override fun write(buf: ByteBuf, packet: CBUpdateViewPositionPlayPacket) {
            buf.writeVarInt(packet.chunkX)
                .writeVarInt(packet.chunkZ)
        }
    }
}