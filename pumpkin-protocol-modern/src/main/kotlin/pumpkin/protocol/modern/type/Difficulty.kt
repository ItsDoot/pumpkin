package pumpkin.protocol.modern.type

import io.netty.buffer.ByteBuf

enum class Difficulty(val id: Int) {
    PEACEFUL(0),
    EASY(1),
    NORMAL(2),
    HEAD(3);

    companion object {
        val FROM_ID: Map<Int, Difficulty> = values().associateBy(Difficulty::id)

        operator fun get(id: Int): Difficulty =
            requireNotNull(values().find { it.id == id }) { "Unknown difficulty: $id" }
    }
}

fun ByteBuf.readDifficulty(): Difficulty =
    Difficulty[this.readByte().toInt()]

fun ByteBuf.writeDifficulty(value: Difficulty): ByteBuf =
    this.writeByte(value.id)