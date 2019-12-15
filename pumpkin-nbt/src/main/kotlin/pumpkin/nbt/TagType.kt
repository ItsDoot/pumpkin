package pumpkin.nbt

import java.io.DataInput
import java.io.DataOutput

enum class TagType(val id: Int, val notchian: String) {
    END(id = 0, notchian = "TAG_End") {
        override val reader: NBTTag.Codec<out NBTTag> = NBTEnd
    },
    BYTE(id = 1, notchian = "TAG_Byte") {
        override val reader: NBTTag.Codec<out NBTTag> = NBTByte
    },
    SHORT(id = 2, notchian = "TAG_Short") {
        override val reader: NBTTag.Codec<out NBTTag> = NBTShort
    },
    INT(id = 3, notchian = "TAG_Int") {
        override val reader: NBTTag.Codec<out NBTTag> = NBTInt
    },
    LONG(id = 4, notchian = "TAG_Long") {
        override val reader: NBTTag.Codec<out NBTTag> = NBTLong
    },
    FLOAT(id = 5, notchian = "TAG_Float") {
        override val reader: NBTTag.Codec<out NBTTag> = NBTFloat
    },
    DOUBLE(id = 6, notchian = "TAG_Double") {
        override val reader: NBTTag.Codec<out NBTTag> = NBTDouble
    },
    BYTE_ARRAY(id = 7, notchian = "TAG_Byte_Array") {
        override val reader: NBTTag.Codec<out NBTTag> = NBTByteArray
    },
    STRING(id = 8, notchian = "TAG_String") {
        override val reader: NBTTag.Codec<out NBTTag> = NBTString
    },
    LIST(id = 9, notchian = "TAG_List") {
        override val reader: NBTTag.Codec<out NBTTag> = NBTList
    },
    COMPOUND(id = 10, notchian = "TAG_Compound") {
        override val reader: NBTTag.Codec<out NBTTag> = NBTCompound
    },
    INT_ARRAY(id = 11, notchian = "TAG_Int_Array") {
        override val reader: NBTTag.Codec<out NBTTag> = NBTIntArray
    },
    LONG_ARRAY(id = 12, notchian = "TAG_Long_Array") {
        override val reader: NBTTag.Codec<out NBTTag> = NBTLongArray
    };

    abstract val reader: NBTTag.Codec<out NBTTag>

    companion object {
        private val ID_TO_TYPE = HashMap<Int, TagType>()

        init {
            for (value in values()) {
                ID_TO_TYPE[value.id] = value
            }
        }

        operator fun get(id: Int): TagType =
            requireNotNull(ID_TO_TYPE[id]) { "Unknown nbt tag type: $id" }
    }
}

fun DataInput.readTagType(): TagType = TagType[this.readByte().toInt()]

fun DataOutput.writeTagType(value: TagType) = this.writeByte(value.id)