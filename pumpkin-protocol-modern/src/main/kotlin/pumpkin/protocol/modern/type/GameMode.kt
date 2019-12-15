package pumpkin.protocol.modern.type

import io.netty.buffer.ByteBuf
import pumpkin.protocol.modern.readVarInt
import pumpkin.protocol.modern.writeVarInt

enum class GameMode(val id: Int) {
    SURVIVAL(0),
    CREATIVE(1),
    ADVENTURE(2),
    SPECTATOR(3);

    companion object {
        val FROM_ID: Map<Int, GameMode> = values().associateBy(GameMode::id)
    }
}

fun ByteBuf.readGameMode(): GameMode {
    val id = this.readVarInt()
    return checkNotNull(GameMode.FROM_ID[id]) { "Unknown gamemode: $id" }
}

fun ByteBuf.writeGameMode(value: GameMode): ByteBuf =
    this.writeVarInt(value.id)