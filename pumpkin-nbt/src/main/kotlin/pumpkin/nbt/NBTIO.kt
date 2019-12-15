package pumpkin.nbt

import java.io.DataInput
import java.io.DataOutput
import java.io.IOException

fun DataInput.readNBT(): NBTTag =
    when (val type = this.readTagType()) {
        TagType.END -> NBTEnd
        else -> {
            // Skip the name
            this.readUTF()

            type.reader.read(this)
        }
    }

fun DataInput.readNamedNBT(): Pair<String?, NBTTag> =
    when (val type = this.readTagType()) {
        TagType.END -> null to NBTEnd
        else -> {
            this.readUTF() to type.reader.read(this)
        }
    }

fun DataInput.readNBTCompound(): NBTCompound {
    val tag = this.readNBT()
    if (tag !is NBTCompound) {
        throw IOException("NBTTag at root was not an NBTCompound")
    }
    return tag
}

fun DataInput.readNamedNBTCompound(): Pair<String?, NBTCompound> {
    val tag = this.readNamedNBT()
    if (tag.second !is NBTCompound) {
        throw IOException("NBTTag at root was not an NBTCompound")
    }
    @Suppress("UNCHECKED_CAST")
    return tag as Pair<String?, NBTCompound>
}

fun DataOutput.writeNBT(tag: NBTTag) {
    this.writeTagType(tag.type)
    if (tag.type != TagType.END) {
        this.writeUTF("")
        tag.write(this)
    }
}

fun DataOutput.writeNamedNBT(name: String, tag: NBTTag) {
    this.writeTagType(tag.type)
    if (tag.type != TagType.END) {
        this.writeUTF(name)
        tag.write(this)
    }
}