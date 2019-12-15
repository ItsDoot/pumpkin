package pumpkin.protocol.modern.nbt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pumpkin.math.vector.Vector2i

@Serializable
data class RegionNBT(
    val columns: Map<Vector2i, Column>
) {

    @Serializable
    data class Column(
        @SerialName("Level")
        val level: Level,
        @SerialName("DataVersion")
        val dataVersion: Int
    )

    @Serializable
    data class Level(
        @SerialName("Heightmaps")
        val heightmaps: Heightmaps
    )

    @Serializable
    data class Heightmaps(
        val motionBlocking: List<Long>,
        val motionBlockingNoLeaves: List<Long>,
        val oceanFloor: List<Long>,
        val worldSurface: List<Long>
    )
}