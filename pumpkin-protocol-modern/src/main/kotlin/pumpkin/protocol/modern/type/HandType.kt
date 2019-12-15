package pumpkin.protocol.modern.type

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.readVarInt
import pumpkin.protocol.modern.writeVarInt

enum class HandType(val id: Int) {
    MAIN_HAND(0),
    OFF_HAND(1);

    companion object {
        operator fun get(id: Int): HandType = when (id) {
            0 -> MAIN_HAND
            1 -> OFF_HAND
            else -> throw IllegalStateException("Unknown hand type: $id")
        }
    }
}

fun ByteBuf.readHandType(): HandType =
    HandType[this.readVarInt()]

fun ByteBuf.writeHandType(value: HandType): ByteBuf =
    this.writeVarInt(value.id)