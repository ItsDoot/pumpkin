package pumpkin.protocol.modern.type

import io.netty.buffer.ByteBuf

class ByteBitSet(backing: Int) {
    constructor(backing: Byte) : this(backing.toInt())
    constructor() : this(0)

    constructor(vararg values: Pair<Int, Boolean>) : this() {
        check(values.size <= 8) { "too many bits" }
        for ((i, value) in values) {
            this[i] = value
        }
    }

    var backing: Int = backing
        private set

    operator fun get(bitIndex: Int): Boolean {
        require(bitIndex in this) { "bitIndex ($bitIndex) must be between 0 and 7" }
        return backing shr bitIndex and 1 == 1
    }

    operator fun set(bitIndex: Int, value: Boolean) {
        require(bitIndex in this) { "bitIndex ($bitIndex) must be between 0 and 7" }
        if (value) {
            // Set the bit
            this.backing = this.backing or (1 shl bitIndex)
        } else {
            // Unset the bit
            this.backing = this.backing and (1 shl bitIndex).inv()
        }
    }

    operator fun contains(bitIndex: Int): Boolean = bitIndex in 0..7

    fun all(vararg bitIndices: Int): Boolean = bitIndices.all { this[it] }

    fun none(vararg bitIndices: Int): Boolean = bitIndices.none { this[it] }

    operator fun component1(): Boolean = this[0]

    operator fun component2(): Boolean = this[1]

    operator fun component3(): Boolean = this[2]

    operator fun component4(): Boolean = this[3]

    operator fun component5(): Boolean = this[4]

    operator fun component6(): Boolean = this[5]

    operator fun component7(): Boolean = this[6]

    operator fun component8(): Boolean = this[7]
}

fun ByteBuf.readByteBitSet(): ByteBitSet {
    val byte = this.readByte()
    println("DEBUG: ${Integer.toBinaryString(byte.toInt())}")
    return ByteBitSet(byte)
}

fun ByteBuf.writeByteBitSet(value: ByteBitSet): ByteBuf {
    println("DEBUG: ${Integer.toBinaryString(value.backing)}")
    return this.writeByte(value.backing)
}

fun main() {
    val bits = "10111101"
    val set = ByteBitSet(bits.toInt(2))
    println(Integer.toBinaryString(set.backing))
    val (invulnerable, flying, allowFlying, creativeMode) = set
    println("$invulnerable, $flying, $allowFlying, $creativeMode")
}