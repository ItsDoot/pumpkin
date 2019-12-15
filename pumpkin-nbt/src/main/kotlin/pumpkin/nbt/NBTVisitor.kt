package pumpkin.nbt

interface NBTVisitor {

    fun visit(tag: NBTEnd)

    fun visit(tag: NBTByte)

    fun visit(tag: NBTShort)

    fun visit(tag: NBTInt)

    fun visit(tag: NBTLong)

    fun visit(tag: NBTFloat)

    fun visit(tag: NBTDouble)

    fun visit(tag: NBTByteArray)

    fun visit(tag: NBTString)

    fun visit(tag: NBTList)

    fun visit(tag: NBTCompound)

    fun visit(tag: NBTIntArray)

    fun visit(tag: NBTLongArray)
}