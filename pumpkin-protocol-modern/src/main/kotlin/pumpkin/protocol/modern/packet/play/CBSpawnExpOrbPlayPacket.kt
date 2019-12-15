package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec

/**
 * Spawns one or more experience orbs.
 */
data class CBSpawnExpOrbPlayPacket(
    val entityId: Int,
    val x: Double,
    val y: Double,
    val z: Double,
    val count: Short
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBSpawnExpOrbPlayPacket> {
        override fun read(buf: ByteBuf): CBSpawnExpOrbPlayPacket =
            CBSpawnExpOrbPlayPacket(
                entityId = buf.readVarInt(),
                x = buf.readDouble(),
                y = buf.readDouble(),
                z = buf.readDouble(),
                count = buf.readShort()
            )

        override fun write(buf: ByteBuf, packet: CBSpawnExpOrbPlayPacket) {
            buf.writeVarInt(packet.entityId)
                .writeDouble(packet.x)
                .writeDouble(packet.y)
                .writeDouble(packet.z)
                .writeShort(packet.count.toInt())
        }
    }
}