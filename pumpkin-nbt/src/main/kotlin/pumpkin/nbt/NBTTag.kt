package pumpkin.nbt

import pumpkin.nbt.util.nullableTypeName
import java.io.DataInput
import java.io.DataOutput
import java.io.IOException

sealed class NBTTag(val type: TagType) {
    abstract fun write(output: DataOutput)

    open fun deepCopy(): NBTTag = this

    abstract fun accept(visitor: NBTVisitor)

    interface Codec<T : NBTTag> {
        fun read(input: DataInput): T

        fun write(output: DataOutput, value: T)
    }
}

sealed class NBTPrimitive(type: TagType) : NBTTag(type) {

    abstract fun asString(): String
}

sealed class NBTNumber(type: TagType) : NBTPrimitive(type) {

    abstract fun asByte(): Byte

    abstract fun asShort(): Short

    abstract fun asInt(): Int

    abstract fun asLong(): Long
}

object NBTEnd : NBTTag(TagType.END), NBTTag.Codec<NBTEnd> {
    override fun write(output: DataOutput) = write(output, this)

    override fun accept(visitor: NBTVisitor) = visitor.visit(this)

    override fun read(input: DataInput): NBTEnd = NBTEnd

    override fun write(output: DataOutput, value: NBTEnd) {}
}

data class NBTByte(val value: Byte) : NBTNumber(TagType.BYTE) {
    override fun write(output: DataOutput) = write(output, this)

    override fun accept(visitor: NBTVisitor) = visitor.visit(this)

    override fun asString(): String = value.toString()

    override fun asByte(): Byte = value

    override fun asShort(): Short = value.toShort()

    override fun asInt(): Int = value.toInt()

    override fun asLong(): Long = value.toLong()

    fun asBoolean(): Boolean = value > 0

    companion object : Codec<NBTByte> {
        override fun read(input: DataInput): NBTByte =
            NBTByte(input.readByte())

        override fun write(output: DataOutput, value: NBTByte) =
            output.writeByte(value.value.toInt())
    }
}

data class NBTShort(val value: Short) : NBTNumber(TagType.SHORT) {
    override fun write(output: DataOutput) = write(output, this)

    override fun accept(visitor: NBTVisitor) = visitor.visit(this)

    override fun asString(): String = value.toString()

    override fun asByte(): Byte = value.toByte()

    override fun asShort(): Short = value

    override fun asInt(): Int = value.toInt()

    override fun asLong(): Long = value.toLong()

    companion object : Codec<NBTShort> {
        override fun read(input: DataInput): NBTShort =
            NBTShort(input.readShort())

        override fun write(output: DataOutput, value: NBTShort) =
            output.writeShort(value.value.toInt())
    }
}

data class NBTInt(val value: Int) : NBTNumber(TagType.INT) {
    override fun write(output: DataOutput) = write(output, this)

    override fun accept(visitor: NBTVisitor) = visitor.visit(this)

    override fun asString(): String = value.toString()

    override fun asByte(): Byte = value.toByte()

    override fun asShort(): Short = value.toShort()

    override fun asInt(): Int = value

    override fun asLong(): Long = value.toLong()

    companion object : Codec<NBTInt> {
        override fun read(input: DataInput): NBTInt =
            NBTInt(input.readInt())

        override fun write(output: DataOutput, value: NBTInt) =
            output.writeByte(value.value)
    }
}

data class NBTLong(val value: Long) : NBTNumber(TagType.LONG) {
    override fun write(output: DataOutput) = write(output, this)

    override fun accept(visitor: NBTVisitor) = visitor.visit(this)

    override fun asString(): String = value.toString()

    override fun asByte(): Byte = value.toByte()

    override fun asShort(): Short = value.toShort()

    override fun asInt(): Int = value.toInt()

    override fun asLong(): Long = value

    companion object : Codec<NBTLong> {
        override fun read(input: DataInput): NBTLong =
            NBTLong(input.readLong())

        override fun write(output: DataOutput, value: NBTLong) =
            output.writeLong(value.value)
    }
}

data class NBTFloat(val value: Float) : NBTPrimitive(TagType.FLOAT) {
    override fun write(output: DataOutput) = write(output, this)

    override fun accept(visitor: NBTVisitor) = visitor.visit(this)

    override fun asString(): String = value.toString()

    companion object : Codec<NBTFloat> {
        override fun read(input: DataInput): NBTFloat =
            NBTFloat(input.readFloat())

        override fun write(output: DataOutput, value: NBTFloat) =
            output.writeFloat(value.value)
    }
}

data class NBTDouble(val value: Double) : NBTPrimitive(TagType.DOUBLE) {
    override fun write(output: DataOutput) = write(output, this)

    override fun accept(visitor: NBTVisitor) = visitor.visit(this)

    override fun asString(): String = value.toString()

    companion object : Codec<NBTDouble> {
        override fun read(input: DataInput): NBTDouble =
            NBTDouble(input.readDouble())

        override fun write(output: DataOutput, value: NBTDouble) =
            output.writeDouble(value.value)
    }
}

data class NBTString(val value: String) : NBTPrimitive(TagType.STRING) {
    override fun write(output: DataOutput) = write(output, this)

    override fun accept(visitor: NBTVisitor) = visitor.visit(this)

    override fun asString(): String = value

    companion object : Codec<NBTString> {
        override fun read(input: DataInput): NBTString =
            NBTString(input.readUTF())

        override fun write(output: DataOutput, value: NBTString) =
            output.writeUTF(value.value)
    }
}

data class NBTList(val valueType: TagType, val values: MutableList<NBTTag>) : NBTTag(TagType.LIST) {
    override fun write(output: DataOutput) = write(output, this)

    override fun deepCopy(): NBTList =
        NBTList(valueType, values.mapTo(ArrayList(), NBTTag::deepCopy))

    override fun accept(visitor: NBTVisitor) = visitor.visit(this)

    operator fun get(index: Int): NBTTag = values[index]

    companion object : Codec<NBTList> {
        override fun read(input: DataInput): NBTList {
            val valueType = input.readTagType()
            val count = input.readInt()
            if (valueType == TagType.END && count > 0) {
                throw IOException("Missing NBTList type")
            } else {
                val values = ArrayList<NBTTag>(count)
                repeat(count) {
                    values += valueType.reader.read(input)
                }
                return NBTList(valueType, values)
            }
        }

        override fun write(output: DataOutput, value: NBTList) {
            if (value.values.isEmpty()) {
                output.writeTagType(TagType.END)
            } else {
                output.writeTagType(value.valueType)
            }

            output.writeInt(value.values.size)

            for (element in value.values) {
                element.write(output)
            }
        }
    }
}

data class NBTCompound(val values: MutableMap<String, NBTTag>) : NBTTag(TagType.COMPOUND) {
    override fun write(output: DataOutput) = write(output, this)

    override fun deepCopy(): NBTCompound =
        NBTCompound(values.mapValuesTo(HashMap()) { (_, value) -> value.deepCopy() })

    override fun accept(visitor: NBTVisitor) = visitor.visit(this)

    fun getOrNull(name: String): NBTTag? = values[name]

    fun get(name: String): NBTTag = requireNotNull(getOrNull(name)) { "$name doesn't exist in this compound" }

    fun getPrimitiveOrNull(name: String): NBTPrimitive? {
        val tag = getOrNull(name)
        require(tag is NBTPrimitive?) { "$name doesn't represent a primitive value (actually was a ${tag.nullableTypeName})" }
        return tag
    }

    fun getPrimitive(name: String): NBTPrimitive = requireNotNull(getPrimitiveOrNull(name)) { "$name doesn't exist in this compound" }

    fun getByteOrNull(name: String): Byte? {
        val tag = getOrNull(name)
        require(tag is NBTByte?) { "$name doesn't represent a Byte (actually was a ${tag.nullableTypeName})" }
        return tag?.value
    }

    fun getByte(name: String): Byte = requireNotNull(getByteOrNull(name)) { "$name doesn't exist in this compound" }

    fun getShortOrNull(name: String): Short? {
        val tag = getOrNull(name)
        require(tag is NBTShort?) { "$name doesn't represent a Short (actually was a ${tag.nullableTypeName})" }
        return tag?.value
    }

    fun getShort(name: String): Short = requireNotNull(getShortOrNull(name)) { "$name doesn't exist in this compound" }

    fun getIntOrNull(name: String): Int? {
        val tag = getOrNull(name)
        require(tag is NBTInt?) { "$name doesn't represent a Int (actually was a ${tag.nullableTypeName})" }
        return tag?.value
    }

    fun getInt(name: String): Int = requireNotNull(getIntOrNull(name)) { "$name doesn't exist in this compound" }

    fun getLongOrNull(name: String): Long? {
        val tag = getOrNull(name)
        require(tag is NBTLong?) { "$name doesn't represent a Long (actually was a ${tag.nullableTypeName})" }
        return tag?.value
    }

    fun getLong(name: String): Long = requireNotNull(getLongOrNull(name)) { "$name doesn't exist in this compound" }

    fun getFloatOrNull(name: String): Float? {
        val tag = getOrNull(name)
        require(tag is NBTFloat?) { "$name doesn't represent a Float (actually was a ${tag.nullableTypeName})" }
        return tag?.value
    }

    fun getFloat(name: String): Float = requireNotNull(getFloatOrNull(name)) { "$name doesn't exist in this compound" }

    fun getDoubleOrNull(name: String): Double? {
        val tag = getOrNull(name)
        require(tag is NBTDouble?) { "$name doesn't represent a Double (actually was a ${tag.nullableTypeName})" }
        return tag?.value
    }

    fun getDouble(name: String): Double = requireNotNull(getDoubleOrNull(name)) { "$name doesn't exist in this compound" }

    fun getByteArrayOrNull(name: String): ByteArray? {
        val tag = getOrNull(name)
        require(tag is NBTByteArray?) { "$name doesn't represent a ByteArray (actually was a ${tag.nullableTypeName})" }
        return tag?.value
    }

    fun getByteArray(name: String): ByteArray = requireNotNull(getByteArrayOrNull(name)) { "$name doesn't exist in this compound" }

    fun getIntArrayOrNull(name: String): IntArray? {
        val tag = getOrNull(name)
        require(tag is NBTIntArray?) { "$name doesn't represent a IntArray (actually was a ${tag.nullableTypeName})" }
        return tag?.value
    }

    fun getIntArray(name: String): IntArray = requireNotNull(getIntArrayOrNull(name)) { "$name doesn't exist in this compound" }

    fun getLongArrayOrNull(name: String): LongArray? {
        val tag = getOrNull(name)
        require(tag is NBTLongArray?) { "$name doesn't represent a LongArray (actually was a ${tag.nullableTypeName})" }
        return tag?.value
    }

    fun getLongArray(name: String): LongArray = requireNotNull(getLongArrayOrNull(name)) { "$name doesn't exist in this compound" }

    fun getStringOrNull(name: String): String? {
        val tag = getOrNull(name)
        require(tag is NBTString?) { "$name doesn't represent a String (actually was a ${tag.nullableTypeName})" }
        return tag?.value
    }

    fun getString(name: String): String = requireNotNull(getStringOrNull(name)) { "$name doesn't exist in this compound" }

    fun getCompoundOrNull(name: String): NBTCompound? {
        val tag = getOrNull(name)
        require(tag is NBTCompound?) { "$name doesn't represent a Compound (actually was a ${tag.nullableTypeName})" }
        return tag
    }

    fun getCompound(name: String): NBTCompound = requireNotNull(getCompoundOrNull(name)) { "$name doesn't exist in this compound" }

    fun getBooleanOrNull(name: String): Boolean? = getByteOrNull(name)?.let { it > 0 }

    fun getBoolean(name: String): Boolean = getByte(name) > 0

    operator fun set(name: String, tag: NBTTag) {
        values[name] = tag
    }

    operator fun contains(name: String): Boolean = name in values

    operator fun iterator(): MutableIterator<MutableMap.MutableEntry<String, NBTTag>> = values.iterator()

    companion object : Codec<NBTCompound> {
        override fun read(input: DataInput): NBTCompound {
            val values = HashMap<String, NBTTag>()
            var curType = input.readTagType()
            while (curType != TagType.END) {
                val name = input.readUTF()
                values[name] = curType.reader.read(input)

                curType = input.readTagType()
            }
            return NBTCompound(values)
        }

        override fun write(output: DataOutput, value: NBTCompound) {
            for ((name, element) in value.values) {
                output.writeTagType(element.type)
                if (element.type != TagType.END) {
                    output.writeUTF(name)
                    element.write(output)
                }
            }
            output.writeTagType(TagType.END)
        }
    }
}

sealed class NBTArraylike<T>(type: TagType) : NBTTag(type) {

    abstract fun asBoxed(): Array<T>
}

data class NBTByteArray(val value: ByteArray) : NBTArraylike<Byte>(TagType.BYTE_ARRAY) {
    override fun write(output: DataOutput) = write(output, this)

    override fun deepCopy(): NBTByteArray = NBTByteArray(value.copyOf())

    override fun accept(visitor: NBTVisitor) = visitor.visit(this)

    override fun asBoxed(): Array<Byte> = value.toTypedArray()

    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        other !is NBTByteArray -> false
        else -> value.contentEquals(other.value)
    }

    override fun hashCode(): Int = value.contentHashCode()

    companion object : Codec<NBTByteArray> {
        override fun read(input: DataInput): NBTByteArray {
            val count = input.readInt()
            val values = ByteArray(count)
            repeat(count) {
                values[it] = input.readByte()
            }
            return NBTByteArray(values)
        }

        override fun write(output: DataOutput, value: NBTByteArray) {
            output.writeInt(value.value.size)
            output.write(value.value)
        }
    }
}

data class NBTIntArray(val value: IntArray) : NBTArraylike<Int>(TagType.INT_ARRAY) {
    override fun write(output: DataOutput) = write(output, this)

    override fun deepCopy(): NBTIntArray = NBTIntArray(value.copyOf())

    override fun accept(visitor: NBTVisitor) = visitor.visit(this)

    override fun asBoxed(): Array<Int> = value.toTypedArray()

    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        other !is NBTIntArray -> false
        else -> value.contentEquals(other.value)
    }

    override fun hashCode(): Int = value.contentHashCode()

    companion object : Codec<NBTIntArray> {
        override fun read(input: DataInput): NBTIntArray {
            val count = input.readInt()
            val values = IntArray(count)
            repeat(count) {
                values[it] = input.readInt()
            }
            return NBTIntArray(values)
        }

        override fun write(output: DataOutput, value: NBTIntArray) {
            output.writeInt(value.value.size)
            for (element in value.value) {
                output.writeInt(element)
            }
        }
    }
}

data class NBTLongArray(val value: LongArray) : NBTArraylike<Long>(TagType.LONG_ARRAY) {
    override fun write(output: DataOutput) = write(output, this)

    override fun deepCopy(): NBTLongArray = NBTLongArray(value.copyOf())

    override fun accept(visitor: NBTVisitor) = visitor.visit(this)

    override fun asBoxed(): Array<Long> = value.toTypedArray()

    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        other !is NBTLongArray -> false
        else -> value.contentEquals(other.value)
    }

    override fun hashCode(): Int = value.contentHashCode()

    companion object : Codec<NBTLongArray> {
        override fun read(input: DataInput): NBTLongArray {
            val count = input.readInt()
            val values = LongArray(count)
            repeat(count) {
                values[it] = input.readLong()
            }
            return NBTLongArray(values)
        }

        override fun write(output: DataOutput, value: NBTLongArray) {
            output.writeInt(value.value.size)
            for (element in value.value) {
                output.writeLong(element)
            }
        }
    }
}