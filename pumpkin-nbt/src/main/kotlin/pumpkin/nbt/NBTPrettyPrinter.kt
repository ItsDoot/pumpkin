package pumpkin.nbt

class NBTPrettyPrinter private constructor(rootName: String?, indent: Int) : NBTVisitor {
    companion object {
        fun toString(tag: NBTTag, rootName: String? = null, indent: Int = 2): String {
            val printer = NBTPrettyPrinter(rootName, indent)
            tag.accept(printer)
            return printer.builder.toString()
        }

        private const val ENTRY = " entry"
        private const val ENTRIES = " entries"
    }

    init {
        check(indent > 0) { "indent must be a positive integer" }
    }

    private val indentStr = " ".repeat(indent)

    private var depth: Int = 0
    private var curName: String? = rootName
    private var builder = StringBuilder()

    private fun StringBuilder.appendType(tag: NBTTag): StringBuilder =
        this.append(tag.type.notchian)

    private fun StringBuilder.appendCurrentName(): StringBuilder =
        curName?.let { this.append("('").append(it).append("')") } ?: this

    private fun StringBuilder.appendArraySize(size: Int, type: String): StringBuilder =
        this.append("[").append(size).append(' ').append(type).append("]")

    private fun StringBuilder.appendEntryCount(count: Int): StringBuilder =
        this.append(count).append(if (count == 1) ENTRY else ENTRIES)

    private fun StringBuilder.appendHeader(tag: NBTTag): StringBuilder =
        this.appendType(tag).appendCurrentName().append(": ")

    private fun StringBuilder.appendDepth(depth: Int): StringBuilder {
        repeat(depth) { this.append(indentStr) }
        return this
    }

    private fun StringBuilder.appendlnAndIndent(): StringBuilder =
        this.appendln().appendDepth(depth)

    private inline fun nested(block: () -> Unit) {
        this.depth++
        block()
        this.depth--
    }

    override fun visit(tag: NBTEnd) {}

    override fun visit(tag: NBTByte) {
        builder.appendHeader(tag).append(tag.value)
    }

    override fun visit(tag: NBTShort) {
        builder.appendHeader(tag).append(tag.value)
    }

    override fun visit(tag: NBTInt) {
        builder.appendHeader(tag).append(tag.value)
    }

    override fun visit(tag: NBTLong) {
        builder.appendHeader(tag).append(tag.value)
    }

    override fun visit(tag: NBTFloat) {
        builder.appendHeader(tag).append(tag.value)
    }

    override fun visit(tag: NBTDouble) {
        builder.appendHeader(tag).append(tag.value)
    }

    override fun visit(tag: NBTByteArray) {
        builder.appendHeader(tag).appendArraySize(tag.value.size, "bytes")
    }

    override fun visit(tag: NBTString) {
        builder.appendHeader(tag).append(tag.value)
    }

    override fun visit(tag: NBTList) {
        builder.appendHeader(tag).appendEntryCount(tag.values.size)

        builder.appendlnAndIndent().append('{')
        nested {
            this.curName = null
            for (value in tag.values) {
                builder.appendlnAndIndent()
                value.accept(this)
            }
        }
        builder.appendlnAndIndent().append('}')
    }

    override fun visit(tag: NBTCompound) {
        builder.appendHeader(tag).appendEntryCount(tag.values.size)

        builder.appendlnAndIndent().append('{')
        nested {
            for ((name, value) in tag.values) {
                this.curName = name
                builder.appendlnAndIndent()
                value.accept(this)
            }
            this.curName = null
        }
        builder.appendlnAndIndent().append('}')
    }

    override fun visit(tag: NBTIntArray) {
        builder.appendHeader(tag).appendArraySize(tag.value.size, "ints")
    }

    override fun visit(tag: NBTLongArray) {
        builder.appendHeader(tag).appendArraySize(tag.value.size, "longs")
    }
}