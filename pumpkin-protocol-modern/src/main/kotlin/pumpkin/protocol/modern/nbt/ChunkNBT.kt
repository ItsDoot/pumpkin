package pumpkin.protocol.modern.nbt

data class ChunkNBT(
    val y: Byte,
    val blockLight: ByteArray,
    val blocks: ByteArray,
    val data: ByteArray,
    val skyLight: ByteArray
) {
    override fun equals(other: Any?): Boolean =
        when {
            this === other -> true
            other !is ChunkNBT -> false
            else -> y == other.y
                    && blockLight.contentEquals(other.blockLight)
                    && blocks.contentEquals(other.blocks)
                    && data.contentEquals(other.data)
                    && skyLight.contentEquals(other.skyLight)
        }

    override fun hashCode(): Int {
        var result = y.toInt()
        result = 31 * result + blockLight.contentHashCode()
        result = 31 * result + blocks.contentHashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + skyLight.contentHashCode()
        return result
    }
}