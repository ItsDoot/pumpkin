package pumpkin.protocol.modern

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import java.util.ArrayList
import java.util.UUID

fun ByteBuf.readRemainingBytes(): ByteArray {
    val result = ByteArray(this.readableBytes())
    this.readBytes(result)
    return result
}

fun ByteBuf.readVarInt(): Int {
    var numRead = 0
    var result = 0
    var read: Int
    do {
        read = this.readByte().toInt()
        val value: Int = read and 0x7F
        result = result or (value shl 7 * numRead)
        numRead++
        check(numRead <= 5) { "VarInt is too big" }
    } while (read and 0x80 != 0)
    return result
}

fun ByteBuf.writeVarInt(value: Int): ByteBuf {
    var value = value
    do {
        var temp = (value and 0x7F)
        value = value ushr 7
        if (value != 0) {
            temp = temp or 0x80
        }
        this.writeByte(temp)
    } while (value != 0)
    return this
}

fun ByteBuf.readVarLong(): Long {
    var numRead = 0
    var result: Long = 0
    var read: Int
    do {
        read = this.readByte().toInt()
        val value: Int = read and 0x7F
        result = result or (value shl 7 * numRead).toLong()
        numRead++
        check(numRead <= 10) { "VarLong is too big" }
    } while (read and 0x80 != 0)
    return result
}

fun ByteBuf.writeVarLong(value: Long): ByteBuf {
    var value = value
    do {
        var temp = (value and 0x7F)
        value = value ushr 7
        if (value != 0L) {
            temp = temp or 0x80
        }
        this.writeByte(temp.toInt())
    } while (value != 0L)
    return this
}

fun ByteBuf.readVarIntString(maxLength: Int = 65536): String {
    val length = this.readVarInt()
    check(length >= 0) { "VarIntString with negative length: $length" }
    check(length <= maxLength * 4) { "VarIntString max length exceeded: $length > ${maxLength * 4}" }
    check(this.isReadable(length)) { "VarIntString is too long: $length > ${this.readableBytes()}" }
    val str = this.toString(this.readerIndex(), length, Charsets.UTF_8)
    this.skipBytes(length)
    return str
}

fun ByteBuf.writeVarIntString(value: String): ByteBuf {
    val size = ByteBufUtil.utf8Bytes(value)
    this.writeVarInt(size)
    ByteBufUtil.writeUtf8(this, value)
    return this
}

fun ByteBuf.readUUID(): UUID =
    UUID(this.readLong(), this.readLong())

fun ByteBuf.writeUUID(value: UUID): ByteBuf =
    this.writeLong(value.mostSignificantBits).writeLong(value.leastSignificantBits)

fun ByteBuf.sizeWrapVarInt(): ByteBuf {
    val result = this.alloc().buffer(this.capacity(), this.maxCapacity())
    result.writeVarInt(this.writerIndex())
    result.writeBytes(this)
    return result
}

fun <T> ByteBuf.readVarIntList(readElement: ByteBuf.() -> T): List<T> {
    val count = this.readVarInt()
    val entries = ArrayList<T>(count)
    repeat(count) {
        entries += this.readElement()
    }
    return entries
}

fun <T> ByteBuf.writeVarIntList(values: List<T>, writeElement: ByteBuf.(T) -> ByteBuf): ByteBuf {
    this.writeVarInt(values.size)
    for (value in values) {
        this.writeElement(value)
    }
    return this
}