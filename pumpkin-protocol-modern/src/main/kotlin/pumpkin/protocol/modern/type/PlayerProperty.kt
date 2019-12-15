package pumpkin.protocol.modern.type

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.readVarIntString
import pumpkin.protocol.modern.writeVarIntString

data class PlayerProperty(
    val name: String,
    val value: String,
    val signature: String?
)

fun ByteBuf.readPlayerProperty(): PlayerProperty =
    PlayerProperty(
        name = this.readVarIntString(),
        value = this.readVarIntString(),
        signature = if (this.readBoolean()) this.readVarIntString() else null
    )

fun ByteBuf.writePlayerProperty(value: PlayerProperty): ByteBuf =
    this.writeVarIntString(value.name)
        .writeVarIntString(value.value)
        .writeBoolean(value.signature != null)
        .apply { if (value.signature != null) this.writeVarIntString(value.signature) }