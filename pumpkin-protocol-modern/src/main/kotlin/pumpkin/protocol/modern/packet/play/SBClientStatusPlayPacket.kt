package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec

data class SBClientStatusPlayPacket(
    val actionId: Int
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<SBClientStatusPlayPacket> {
        override fun read(buf: ByteBuf): SBClientStatusPlayPacket =
            SBClientStatusPlayPacket(
                actionId = buf.readVarInt()
            )

        override fun write(buf: ByteBuf, packet: SBClientStatusPlayPacket) {
            buf.writeVarInt(packet.actionId)
        }
    }
}