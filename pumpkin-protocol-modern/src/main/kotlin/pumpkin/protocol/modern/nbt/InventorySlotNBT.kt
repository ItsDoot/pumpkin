package pumpkin.protocol.modern.nbt

data class InventorySlotNBT(
    val count: Byte,
    val slot: Byte,
    val damage: Short,
    val id: Short
)