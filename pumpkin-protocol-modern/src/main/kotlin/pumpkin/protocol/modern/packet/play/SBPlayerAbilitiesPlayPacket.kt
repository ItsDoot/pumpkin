package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler
import pumpkin.protocol.modern.type.ByteBitSet
import pumpkin.protocol.modern.type.readByteBitSet
import pumpkin.protocol.modern.type.writeByteBitSet

data class SBPlayerAbilitiesPlayPacket(
    val instantBreak: Boolean,
    val flying: Boolean,
    val canFly: Boolean,
    val invulnerable: Boolean,
    val flySpeed: Float,
    val walkSpeed: Float
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<SBPlayerAbilitiesPlayPacket> {
        override fun read(buf: ByteBuf): SBPlayerAbilitiesPlayPacket {
            val (instantBreak: Boolean, flying: Boolean, canFly: Boolean, invulnerable: Boolean) =
                buf.readByteBitSet()

            return SBPlayerAbilitiesPlayPacket(
                instantBreak = instantBreak,
                flying = flying,
                canFly = canFly,
                invulnerable = invulnerable,
                flySpeed = buf.readFloat(),
                walkSpeed = buf.readFloat()
            )
        }

        override fun write(buf: ByteBuf, packet: SBPlayerAbilitiesPlayPacket) {
            buf.writeByteBitSet(
                ByteBitSet(
                    0 to packet.instantBreak,
                    1 to packet.flying,
                    2 to packet.canFly,
                    3 to packet.invulnerable
                )
            )
                .writeFloat(packet.flySpeed)
                .writeFloat(packet.walkSpeed)
        }
    }
}