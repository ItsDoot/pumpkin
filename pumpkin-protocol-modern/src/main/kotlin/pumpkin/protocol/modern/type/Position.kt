package pumpkin.protocol.modern.type

import io.netty.buffer.ByteBuf

inline class Position(val value: Long) {
    constructor(x: Int, y: Int, z: Int) :
            this((((x and 0x3FFFFFF) shl 38) or ((z and 0x3FFFFFF) shl 12) or (y and 0xFFF)).toLong())

    val x: Int
        get() = (value shr 38).toInt()

    val y: Int
        get() = (value and 0xFFF).toInt()

    val z: Int
        get() = (value shl 26 shr 38).toInt()

    override fun toString(): String = "Position(x=$x, y=$y, z=$z)"
}

fun ByteBuf.readPosition(): Position =
    Position(this.readLong())

fun ByteBuf.writePosition(value: Position): ByteBuf =
    this.writeLong(value.value)