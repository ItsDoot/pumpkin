package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.*
import pumpkin.protocol.core.PacketCodec

data class SBClientSettingsPlayPacket(
    val locale: String,
    val viewDistance: Int,
    val chatMode: Int,
    val chatColors: Boolean,
    val displayedSkinParts: Int,
    val mainHand: Int
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<SBClientSettingsPlayPacket> {
        override fun read(buf: ByteBuf): SBClientSettingsPlayPacket =
            SBClientSettingsPlayPacket(
                locale = buf.readVarIntString(16),
                viewDistance = buf.readByte().toInt(),
                chatMode = buf.readVarInt(),
                chatColors = buf.readBoolean(),
                displayedSkinParts = buf.readUnsignedByte().toInt(),
                mainHand = buf.readVarInt()
            )

        override fun write(buf: ByteBuf, packet: SBClientSettingsPlayPacket) {
            buf.writeVarIntString(packet.locale)
                .writeByte(packet.viewDistance)
                .writeVarInt(packet.chatMode)
                .writeBoolean(packet.chatColors)
                .writeByte(packet.displayedSkinParts)
                .writeVarInt(packet.mainHand)
        }
    }
}