package pumpkin.protocol.modern.packet.status

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler

object SBRequestStatusPacket : ModernPacket.Status(), PacketCodec<SBRequestStatusPacket> {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    override fun read(buf: ByteBuf): SBRequestStatusPacket = SBRequestStatusPacket

    override fun write(buf: ByteBuf, packet: SBRequestStatusPacket) {}

    override fun toString(): String = "StatusRequestPacket"
}