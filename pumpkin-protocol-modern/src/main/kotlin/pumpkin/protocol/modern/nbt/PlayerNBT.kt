package pumpkin.protocol.modern.nbt

import pumpkin.math.vector.Vector2f
import pumpkin.math.vector.Vector3d
import pumpkin.nbt.NBTCompound

data class PlayerNBT(
    val onGround: Boolean,
    val sleeping: Boolean,
    val sleepTimer: Short,
    val air: Short,
    val attackTime: Short,
    val deathTime: Short,
    val fire: Short,
    val health: Short,
    val hurtTime: Short,
    val foodLevel: Int,
    val foodExhaustionLevel: Float,
    val foodSaturationLevel: Float,
    val foodTickTimer: Int,
    val xpLevel: Int,
    val xpTotal: Int,
    val xpPercent: Float,
    val dimension: Int,
    val playerGameType: Int,
    val fallDistance: Float,
    val inventory: List<InventorySlotNBT>,
    val position: Vector3d,
    val velocity: Vector3d,
    val rotation: Vector2f
) {

    companion object {

        fun from(nbt: NBTCompound): PlayerNBT = TODO()
    }
}