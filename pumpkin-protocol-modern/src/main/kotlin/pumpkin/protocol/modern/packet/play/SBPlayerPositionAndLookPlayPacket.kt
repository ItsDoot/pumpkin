package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler

data class SBPlayerPositionAndLookPlayPacket(
    val x: Double,
    val feetY: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float,
    val onGround: Boolean
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<SBPlayerPositionAndLookPlayPacket> {
        override fun read(buf: ByteBuf): SBPlayerPositionAndLookPlayPacket =
            SBPlayerPositionAndLookPlayPacket(
                x = buf.readDouble(),
                feetY = buf.readDouble(),
                z = buf.readDouble(),
                yaw = buf.readFloat(),
                pitch = buf.readFloat(),
                onGround = buf.readBoolean()
            )

        override fun write(buf: ByteBuf, packet: SBPlayerPositionAndLookPlayPacket) {
            buf.writeDouble(packet.x)
                .writeDouble(packet.feetY)
                .writeDouble(packet.z)
                .writeFloat(packet.yaw)
                .writeFloat(packet.pitch)
                .writeBoolean(packet.onGround)
        }
    }
}