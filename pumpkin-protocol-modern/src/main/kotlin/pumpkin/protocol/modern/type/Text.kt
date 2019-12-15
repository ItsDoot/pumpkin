package pumpkin.protocol.modern.type

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.readVarIntString
import pumpkin.protocol.modern.writeVarIntString
import pumpkin.text.Text

fun ByteBuf.readText(): Text =
    Text.fromJsonString(this.readVarIntString())

fun ByteBuf.writeText(value: Text): ByteBuf =
    this.writeVarIntString(value.toJsonString())