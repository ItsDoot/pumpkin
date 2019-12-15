package pumpkin.protocol.modern.nbt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LevelNBT(
    @SerialName("Data")
    val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("DataPacks")
        val dataPacks: DataPacks,
        @SerialName("GameRules")
        val gameRules: Map<String, String>,
        @SerialName("Version")
        val levelVersion: LevelVersion,
        val allowCommands: Boolean,
        @SerialName("BorderCenterX")
        val borderCenterX: Double,
        @SerialName("BorderCenterZ")
        val borderCenterZ: Double,
        @SerialName("BorderDamagePerBlock")
        val borderDamagePerBlock: Double,
        @SerialName("BorderSafeZone")
        val borderSafeZone: Double,
        @SerialName("BorderSize")
        val borderSize: Double,
        @SerialName("BorderSizeLerpTarget")
        val borderSizeLerpTarget: Double,
        @SerialName("BorderSizeLerpTime")
        val borderSizeLerpTime: Long,
        @SerialName("BorderWarningBlocks")
        val borderWarningBlocks: Double,
        @SerialName("BorderWarningTime")
        val borderWarningTime: Double,
        val clearWeatherTime: Int,
        @SerialName("DataVersion")
        val dataVersion: Int,
        @SerialName("DayTime")
        val dayTime: Long,
        @SerialName("Difficulty")
        val difficulty: Byte,
        @SerialName("DifficultyLocked")
        val difficultyLocked: Boolean,
        @SerialName("GameType")
        val gameType: Int,
        val generatorName: String,
        val generatorVersion: Int,
        val hardcore: Boolean,
        val initialized: Boolean,
        @SerialName("LastPlayed")
        val lastPlayed: Long,
        @SerialName("LevelName")
        val levelName: String,
        @SerialName("MapFeatures")
        val mapFeatures: Boolean,
        val raining: Boolean,
        val rainTime: Int,
        @SerialName("RandomSeed")
        val seed: Long,
        @SerialName("SizeOnDisk")
        val sizeOnDisk: Long,
        @SerialName("SpawnX")
        val spawnX: Int,
        @SerialName("SpawnY")
        val spawnY: Int,
        @SerialName("SpawnZ")
        val spawnZ: Int,
        val thundering: Boolean,
        val thunderTime: Int,
        @SerialName("Time")
        val time: Long,
        val version: Int,
        @SerialName("WanderingTraderSpawnChance")
        val wanderingTraderSpawnChance: Int,
        @SerialName("WanderingTraderSpawnDelay")
        val wanderingTraderSpawnDelay: Int
    )

    @Serializable
    data class LevelVersion(
        @SerialName("Id")
        val id: Int,
        @SerialName("Name")
        val name: String,
        @SerialName("Snapshot")
        val snapshot: Boolean
    )

    @Serializable
    data class DataPacks(
        @SerialName("Enabled")
        val enabled: List<String>,
        @SerialName("Disabled")
        val disabled: List<String>
    )
}