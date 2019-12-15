package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.type.ByteBitSet
import pumpkin.protocol.modern.type.readByteBitSet
import pumpkin.protocol.modern.type.writeByteBitSet

data class CBPlayerAbilitiesPlayPacket(
    val invulnerable: Boolean,
    val flying: Boolean,
    val canFly: Boolean,
    val instantBreak: Boolean,
    val flySpeed: Float,
    val fovModifier: Float
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBPlayerAbilitiesPlayPacket> {
        override fun read(buf: ByteBuf): CBPlayerAbilitiesPlayPacket {
            val (invulnerable: Boolean, flying: Boolean, canFly: Boolean, instantBreak: Boolean) =
                buf.readByteBitSet()

            return CBPlayerAbilitiesPlayPacket(
                invulnerable = invulnerable,
                flying = flying,
                canFly = canFly,
                instantBreak = instantBreak,
                flySpeed = buf.readFloat(),
                fovModifier = buf.readFloat()
            )
        }

        override fun write(buf: ByteBuf, packet: CBPlayerAbilitiesPlayPacket) {
            buf.writeByteBitSet(
                ByteBitSet(
                    0 to packet.invulnerable,
                    1 to packet.flying,
                    2 to packet.canFly,
                    3 to packet.instantBreak
                )
            )
                .writeFloat(packet.flySpeed)
                .writeFloat(packet.fovModifier)
        }
    }
}