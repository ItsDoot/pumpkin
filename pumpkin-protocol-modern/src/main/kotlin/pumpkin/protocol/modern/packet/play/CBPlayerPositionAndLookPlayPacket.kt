package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.type.ByteBitSet
import pumpkin.protocol.modern.type.readByteBitSet
import pumpkin.protocol.modern.type.writeByteBitSet

data class CBPlayerPositionAndLookPlayPacket(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float,
    val relativeX: Boolean,
    val relativeY: Boolean,
    val relativeZ: Boolean,
    val relativePitch: Boolean,
    val relativeYaw: Boolean,
    val teleportId: Int
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBPlayerPositionAndLookPlayPacket> {
        val ZERO = CBPlayerPositionAndLookPlayPacket(
            x = 0.0, y = 0.0, z = 0.0,
            yaw = 0.0f, pitch = 0.0f,
            relativeX = false, relativeY = false, relativeZ = false,
            relativePitch = false, relativeYaw = false,
            teleportId = 0
        )

        override fun read(buf: ByteBuf): CBPlayerPositionAndLookPlayPacket {
            val x = buf.readDouble()
            val y = buf.readDouble()
            val z = buf.readDouble()
            val yaw = buf.readFloat()
            val pitch = buf.readFloat()
            val (relX: Boolean, relY: Boolean, relZ: Boolean, relPitch: Boolean, relYaw: Boolean) =
                buf.readByteBitSet()

            return CBPlayerPositionAndLookPlayPacket(
                x = x,
                y = y,
                z = z,
                yaw = yaw,
                pitch = pitch,
                relativeX = relX,
                relativeY = relY,
                relativeZ = relZ,
                relativePitch = relPitch,
                relativeYaw = relYaw,
                teleportId = buf.readVarInt()
            )
        }

        override fun write(buf: ByteBuf, packet: CBPlayerPositionAndLookPlayPacket) {
            buf.writeDouble(packet.x)
                .writeDouble(packet.y)
                .writeDouble(packet.z)
                .writeFloat(packet.yaw)
                .writeFloat(packet.pitch)
                .writeByteBitSet(
                    ByteBitSet(0 to packet.relativeX, 1 to packet.relativeY, 2 to packet.relativeZ,
                        3 to packet.relativePitch, 4 to packet.relativeYaw)
                )
                .writeVarInt(packet.teleportId)
        }
    }
}