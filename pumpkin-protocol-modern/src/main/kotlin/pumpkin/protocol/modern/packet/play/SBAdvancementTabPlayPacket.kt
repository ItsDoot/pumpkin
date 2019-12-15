package pumpkin.protocol.modern.packet.play

import io.netty.buffer.ByteBuf
import pumpkin.protocol.core.PacketCodec
import pumpkin.protocol.modern.ModernPacket
import pumpkin.protocol.modern.ModernPacketHandler
import pumpkin.protocol.modern.readVarInt
import pumpkin.protocol.modern.type.Identifier
import pumpkin.protocol.modern.type.readIdentifier
import pumpkin.protocol.modern.type.writeIdentifier
import pumpkin.protocol.modern.writeVarInt

data class SBAdvancementTabPlayPacket(
    val action: Action,
    val tabId: Identifier?
) : ModernPacket.Play() {

    override fun handle(handler: ModernPacketHandler) {
        handler.handle(this)
    }

    enum class Action(val id: Int) {
        OPENED_TAB(0),
        CLOSED_SCREEN(1)
    }

    companion object : PacketCodec<SBAdvancementTabPlayPacket> {
        override fun read(buf: ByteBuf): SBAdvancementTabPlayPacket {
            val action: Action = buf.readAction()
            val tabId: Identifier? = if (action == Action.OPENED_TAB) buf.readIdentifier() else null
            return SBAdvancementTabPlayPacket(action, tabId)
        }

        override fun write(buf: ByteBuf, packet: SBAdvancementTabPlayPacket) {
            buf.writeAction(packet.action)
            if (packet.action == Action.OPENED_TAB)
                buf.writeIdentifier(requireNotNull(packet.tabId) { "tabId must not be null for Action.OPENED_TAB" })
        }
    }
}

fun ByteBuf.readAction(): SBAdvancementTabPlayPacket.Action = when (val id = this.readVarInt()) {
    0 -> SBAdvancementTabPlayPacket.Action.OPENED_TAB
    1 -> SBAdvancementTabPlayPacket.Action.CLOSED_SCREEN
    else -> throw IllegalArgumentException("Unknown advancement tab action id: $id")
}

fun ByteBuf.writeAction(action: SBAdvancementTabPlayPacket.Action): ByteBuf =
    this.writeVarInt(action.id)