package pumpkin.nbt.serialization.internal

import kotlinx.serialization.*
import kotlinx.serialization.modules.SerialModule
import pumpkin.nbt.*
import pumpkin.nbt.serialization.NBT
import pumpkin.nbt.serialization.NBTDecodingException
import pumpkin.nbt.serialization.NBTInput
import pumpkin.nbt.serialization.PRIMITIVE_TAG

internal fun <T> NBT.readNBT(tag: NBTTag, deserializer: DeserializationStrategy<T>): T {
    val input = when (tag) {
        is NBTCompound -> NBTTreeInput(this, tag)
        is NBTList -> NBTTreeListInput(this, tag)
        is NBTPrimitive -> NBTPrimitiveInput(this, tag)
        else -> TODO()
    }
    return input.decode(deserializer)
}

internal inline fun <reified T : NBTTag> NBTTag.cast(): T {
    require(this is T) { "Expected ${T::class} but found ${this::class}" }
    return this
}

private sealed class AbstractNBTTreeInput(override val nbt: NBT, open val obj: NBTTag) : NamedValueDecoder(),
    NBTInput {

    override val context: SerialModule
        get() = nbt.context

    private fun currentObject(): NBTTag = currentTagOrNull?.let { currentElement(it) } ?: obj

    override fun decodeNBT(): NBTTag = currentObject()

    override val updateMode: UpdateMode
        get() = nbt.updateMode

    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
        return decodeSerializableValuePolymorphic(deserializer)
    }

    override fun composeName(parentName: String, childName: String): String = childName

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeDecoder {
        val currentObject = currentObject()
        return when (desc.kind) {
            StructureKind.LIST, is PolymorphicKind -> NBTTreeListInput(
                nbt,
                currentObject.cast()
            )
            StructureKind.MAP -> NBTTreeMapInput(nbt, currentObject.cast())
            else -> NBTTreeInput(nbt, currentObject.cast())
        }
    }

    protected inline fun <reified T : NBTPrimitive> getValue(tag: String): T {
        val currentElement = currentElement(tag)
        return currentElement as? T ?: throw NBTDecodingException(
            -1,
            "Expected ${T::class} at $tag, found $currentElement"
        )
    }

    protected abstract fun currentElement(tag: String): NBTTag

    override fun decodeTaggedChar(tag: String): Char {
        val str = getValue<NBTString>(tag).asString()
        return if (str.length == 1) str[0] else throw SerializationException("$str can't be represented as Char")
    }

    override fun decodeTaggedEnum(tag: String, enumDescription: SerialDescriptor): Int =
        getValue<NBTByte>(tag).asInt()

    override fun decodeTaggedNull(tag: String): Nothing? = null

    override fun decodeTaggedNotNullMark(tag: String): Boolean = true

    override fun decodeTaggedUnit(tag: String) {}

    override fun decodeTaggedBoolean(tag: String): Boolean = decodeTaggedByte(tag) > 0

    override fun decodeTaggedByte(tag: String): Byte = getValue<NBTByte>(tag).value

    override fun decodeTaggedShort(tag: String): Short = getValue<NBTShort>(tag).value

    override fun decodeTaggedInt(tag: String): Int = getValue<NBTInt>(tag).value

    override fun decodeTaggedLong(tag: String): Long = getValue<NBTLong>(tag).value

    override fun decodeTaggedFloat(tag: String): Float = getValue<NBTFloat>(tag).value

    override fun decodeTaggedDouble(tag: String): Double = getValue<NBTDouble>(tag).value

    override fun decodeTaggedString(tag: String): String = getValue<NBTString>(tag).value
}


private class NBTPrimitiveInput(nbt: NBT, override val obj: NBTPrimitive) : AbstractNBTTreeInput(nbt, obj) {

    init {
        pushTag(PRIMITIVE_TAG)
    }

    override fun currentElement(tag: String): NBTPrimitive {
        require(tag == PRIMITIVE_TAG) { "This input can only handle primitives with '$PRIMITIVE_TAG' tag" }
        return obj
    }
}

private open class NBTTreeInput(nbt: NBT, override val obj: NBTCompound) : AbstractNBTTreeInput(nbt, obj) {
    private var position = 0

    override fun decodeElementIndex(desc: SerialDescriptor): Int {
        while (position < desc.elementsCount) {
            val name = desc.getTag(position++)
            if (name in obj) {
                return position - 1
            }
        }
        return CompositeDecoder.READ_DONE
    }

    override fun currentElement(tag: String): NBTTag = obj.get(tag)

    override fun endStructure(desc: SerialDescriptor) {}
}

private class NBTTreeMapInput(nbt: NBT, override val obj: NBTCompound) : NBTTreeInput(nbt, obj) {
    private val keys = obj.values.keys.toList()
    private val size: Int = keys.size * 2
    private var position = -1

    override fun elementName(desc: SerialDescriptor, index: Int): String {
        val i = index / 2
        return keys[i]
    }

    override fun decodeElementIndex(desc: SerialDescriptor): Int {
        while (position < size - 1) {
            position++
            return position
        }
        return CompositeDecoder.READ_DONE
    }

    override fun currentElement(tag: String): NBTTag =
        if (position % 2 == 0) NBTString(tag) else obj.get(tag)

    override fun endStructure(desc: SerialDescriptor) {}
}

private class NBTTreeListInput(nbt: NBT, override val obj: NBTList) : AbstractNBTTreeInput(nbt, obj) {
    private val size = obj.values.size
    private var currentIndex = -1

    override fun elementName(desc: SerialDescriptor, index: Int): String = index.toString()

    override fun currentElement(tag: String): NBTTag = obj[tag.toInt()]

    override fun decodeElementIndex(desc: SerialDescriptor): Int {
        while (currentIndex < size - 1) {
            currentIndex++
            return currentIndex
        }
        return CompositeDecoder.READ_DONE
    }
}