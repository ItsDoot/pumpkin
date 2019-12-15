package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec

data class SBSetBeaconEffectPlayPacket(
    val primaryEffect: Int,
    val secondaryEffect: Int
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<SBSetBeaconEffectPlayPacket> {
        override fun read(buf: ByteBuf): SBSetBeaconEffectPlayPacket =
            SBSetBeaconEffectPlayPacket(
                primaryEffect = buf.readVarInt(),
                secondaryEffect = buf.readVarInt()
            )

        override fun write(buf: ByteBuf, packet: SBSetBeaconEffectPlayPacket) {
            buf.writeVarInt(packet.primaryEffect)
                .writeVarInt(packet.secondaryEffect)
        }
    }
}