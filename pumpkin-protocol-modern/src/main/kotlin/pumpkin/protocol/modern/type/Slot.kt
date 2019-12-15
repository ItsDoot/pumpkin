package pumpkin.protocol.modern.type

import io.netty.buffer.ByteBuf
import pumpkin.nbt.NBTTag
import pumpkin.protocol.modern.readVarInt
import pumpkin.protocol.modern.writeVarInt

data class Slot(
    val itemId: Int,
    val itemCount: Int,
    val nbt: NBTTag
)

fun ByteBuf.readSlot(): Slot? {
    val present = this.readBoolean()
    return if (present) Slot(this.readVarInt(), this.readByte().toInt(), this.readNBT()) else null
}

fun ByteBuf.writeSlot(slot: Slot?): ByteBuf {
    this.writeBoolean(slot != null)
    if (slot != null) {
        this.writeVarInt(slot.itemId)
        this.writeByte(slot.itemCount)
        this.writeNBT(slot.nbt)
    }
    return this
}