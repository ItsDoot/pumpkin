package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler
import pumpkin.protocol.modern.type.Tag
import pumpkin.protocol.modern.type.readTagList
import pumpkin.protocol.modern.type.writeTagList

data class CBTagsPlayPacket(
    val blockTags: List<Tag>,
    val itemTags: List<Tag>,
    val fluidTags: List<Tag>,
    val entityTags: List<Tag>
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<CBTagsPlayPacket> {
        val EMPTY = CBTagsPlayPacket(emptyList(), emptyList(), emptyList(), emptyList())

        override fun read(buf: ByteBuf): CBTagsPlayPacket =
            CBTagsPlayPacket(
                blockTags = buf.readTagList(),
                itemTags = buf.readTagList(),
                fluidTags = buf.readTagList(),
                entityTags = buf.readTagList()
            )

        override fun write(buf: ByteBuf, packet: CBTagsPlayPacket) {
            buf.writeTagList(packet.blockTags)
                .writeTagList(packet.itemTags)
                .writeTagList(packet.fluidTags)
                .writeTagList(packet.entityTags)
        }
    }
}