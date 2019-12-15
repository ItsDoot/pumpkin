package pumpkin.protocol.modern.type

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.readVarInt
import pumpkin.protocol.modern.writeVarInt

data class Tag(
    val name: Identifier,
    val entries: List<Int>
)

fun ByteBuf.readTag(): Tag {
    val name = this.readIdentifier()
    val count = this.readVarInt()
    val entries = ArrayList<Int>(count)
    repeat(count) {
        entries.add(this.readVarInt())
    }
    return Tag(name, entries)
}

fun ByteBuf.writeTag(value: Tag): ByteBuf =
    this.writeIdentifier(value.name)
        .writeVarInt(value.entries.size)
        .apply {
            val iter = value.entries.iterator()
            while (iter.hasNext()) {
                this.writeVarInt(iter.next())
            }
        }

fun ByteBuf.readTagList(): List<Tag> {
    val length = this.readVarInt()
    val entries = ArrayList<Tag>(length)
    repeat(length) {
        entries += this.readTag()
    }
    return entries
}

fun ByteBuf.writeTagList(values: List<Tag>): ByteBuf =
    this.writeVarInt(values.size)
        .apply {
            for (value in values) {
                this.writeTag(value)
            }
        }