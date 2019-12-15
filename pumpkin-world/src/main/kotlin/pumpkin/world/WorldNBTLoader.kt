package pumpkin.world

import pumpkin.nbt.loader.NBTLoader
import pumpkin.nbt.serialization.NBT
import pumpkin.protocol.modern.nbt.LevelNBT
import pumpkin.protocol.modern.nbt.PlayerNBT
import pumpkin.protocol.modern.nbt.RegionNBT
import pumpkin.util.div
import java.nio.file.Path
import java.util.UUID

data class WorldNBTLoader(val folder: Path): NBTLoader() {

    fun loadLevel(): LevelNBT = NBT.plain.readNBT(LevelNBT.serializer(), load(folder / "level.dat"))

    fun saveLevel(nbt: LevelNBT): Unit = TODO()

    fun loadPlayer(uniqueId: UUID): PlayerNBT = PlayerNBT.from(load(folder / "players" / "$uniqueId.dat"))

    fun savePlayer(uniqueId: UUID, nbt: PlayerNBT): Unit = TODO()

    fun loadRegion(regionX: Int, regionZ: Int): RegionNBT = TODO()
}