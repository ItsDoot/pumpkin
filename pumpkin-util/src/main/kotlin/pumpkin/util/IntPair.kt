package pumpkin.util

inline class IntPair(val value: Long) {
    constructor(first: Int, second: Int) :
            this((first.toLong() shl 32) or (second.toLong() and 0xffffffffL))

    val first: Int get() = (value shr 32).toInt()

    val second: Int get() = value.toInt()
}