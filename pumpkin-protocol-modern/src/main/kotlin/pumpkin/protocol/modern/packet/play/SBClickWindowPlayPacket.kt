package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler
import pumpkin.protocol.modern.readVarInt
import pumpkin.protocol.modern.type.InventoryOperationType
import pumpkin.protocol.modern.type.Slot
import pumpkin.protocol.modern.type.readSlot
import pumpkin.protocol.modern.type.writeSlot
import pumpkin.protocol.modern.writeVarInt

data class SBClickWindowPlayPacket(
    val windowId: Int,
    val slot: Int,
    val actionNum: Int,
    val operation: InventoryOperationType,
    val clickedItem: Slot?
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    companion object : PacketCodec<SBClickWindowPlayPacket> {
        override fun read(buf: ByteBuf): SBClickWindowPlayPacket {
            val windowId = buf.readUnsignedByte().toInt()
            val slot = buf.readShort().toInt()
            val button = buf.readByte().toInt()
            val actionNum = buf.readShort().toInt()
            val mode = buf.readVarInt()
            val clickedItem = buf.readSlot()
            return SBClickWindowPlayPacket(windowId, slot, actionNum, InventoryOperationType[mode, button], clickedItem)
        }

        override fun write(buf: ByteBuf, packet: SBClickWindowPlayPacket) {
            buf.writeByte(packet.windowId)
                .writeShort(packet.slot)
                .writeByte(packet.operation.button)
                .writeShort(packet.actionNum)
                .writeVarInt(packet.operation.mode)
                .writeSlot(packet.clickedItem)
        }
    }
}