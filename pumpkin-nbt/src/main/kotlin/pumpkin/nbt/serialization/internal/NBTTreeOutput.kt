package pumpkin.nbt.serialization.internal

import kotlinx.serialization.NamedValueEncoder
import kotlinx.serialization.modules.SerialModule
import pumpkin.nbt.NBTByte
import pumpkin.nbt.NBTDouble
import pumpkin.nbt.NBTFloat
import pumpkin.nbt.NBTInt
import pumpkin.nbt.NBTLong
import pumpkin.nbt.NBTShort
import pumpkin.nbt.NBTString
import pumpkin.nbt.NBTTag
import pumpkin.nbt.serialization.NBT
import pumpkin.nbt.serialization.NBTOutput
import pumpkin.nbt.serialization.NBTTagSerializer

private sealed class AbstractNBTTreeOutput(override val nbt: NBT, val consumer: (NBTTag) -> Unit) : NamedValueEncoder(), NBTOutput {

    override val context: SerialModule
        get() = nbt.context

    override fun encodeNBT(tag: NBTTag) {
        encodeSerializableValue(NBTTagSerializer, tag)
    }

    override fun composeName(parentName: String, childName: String): String = childName

    abstract fun putElement(key: String, element: NBTTag)
    abstract fun getCurrent(): NBTTag

    override fun encodeTaggedBoolean(tag: String, value: Boolean) = putElement(tag, NBTByte(if (value) 1 else 0))

    override fun encodeTaggedByte(tag: String, value: Byte) = putElement(tag, NBTByte(value))
    override fun encodeTaggedShort(tag: String, value: Short) = putElement(tag, NBTShort(value))
    override fun encodeTaggedInt(tag: String, value: Int) = putElement(tag, NBTInt(value))
    override fun encodeTaggedLong(tag: String, value: Long) = putElement(tag, NBTLong(value))

    override fun encodeTaggedFloat(tag: String, value: Float) = putElement(tag, NBTFloat(value))
    override fun encodeTaggedDouble(tag: String, value: Double) = putElement(tag, NBTDouble(value))

    override fun encodeTaggedChar(tag: String, value: Char) = putElement(tag, NBTString(value.toString()))
    override fun encodeTaggedString(tag: String, value: String) = putElement(tag, NBTString(value))

    override fun encodeTaggedValue(tag: String, value: Any) = putElement(tag, NBTString(value.toString()))


}