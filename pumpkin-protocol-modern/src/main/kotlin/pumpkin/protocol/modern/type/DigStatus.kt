package pumpkin.protocol.modern.type

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.readVarInt
import pumpkin.protocol.modern.writeVarInt

enum class DigStatus(val id: Int) {
    STARTED_DIGGING(0),
    CANCELLING_DIGGING(1),
    FINISHED_DIGGING(2),
    DROP_ITEM_STACK(3),
    DROP_ITEM(4),
    FINISH_ITEM_USE(5),
    SWAP_ITEM_IN_HAND(6);

    companion object {
        private val idMap: Map<Int, DigStatus> = values().associateBy { it.id }

        operator fun get(id: Int): DigStatus =
            requireNotNull(this.idMap[id]) { "Unknown dig status: $id" }
    }
}

fun ByteBuf.readDigStatus(): DigStatus = DigStatus[this.readVarInt()]

fun ByteBuf.writeDigStatus(status: DigStatus): ByteBuf = this.writeVarInt(status.id)