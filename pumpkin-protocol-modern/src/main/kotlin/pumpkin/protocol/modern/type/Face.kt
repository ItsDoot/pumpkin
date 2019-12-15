package pumpkin.protocol.modern.type

import io.netty.buffer.ByteBuf

enum class Face(val id: Int) {
    BOTTOM(0),
    TOP(1),
    NORTH(2),
    SOUTH(3),
    WEST(4),
    EAST(5);

    companion object {
        private val idMap: Map<Int, Face> = values().associateBy { it.id }

        operator fun get(id: Int): Face =
            requireNotNull(this.idMap[id]) { "Unknown face id: $id" }
    }
}

fun ByteBuf.readFace(): Face = Face[this.readByte().toInt()]

fun ByteBuf.writeFace(face: Face): ByteBuf = this.writeByte(face.id)