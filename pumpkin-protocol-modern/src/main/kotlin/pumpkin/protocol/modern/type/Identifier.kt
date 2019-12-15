package pumpkin.protocol.modern.type

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.readVarIntString
import pumpkin.protocol.modern.writeVarIntString

data class Identifier(val namespace: String, val value: String) {
    override fun toString(): String = "$namespace:$value"

    companion object {
        operator fun invoke(delimitedName: String): Identifier {
            val (namespace, value) = delimitedName.split(':')
            return Identifier(namespace, value)
        }
    }
}

fun ByteBuf.readIdentifier(): Identifier =
    Identifier(this.readVarIntString())

fun ByteBuf.writeIdentifier(value: Identifier): ByteBuf =
    this.writeVarIntString(value.toString())