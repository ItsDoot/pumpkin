package pumpkin.protocol.modern.nbt

import pumpkin.math.vector.Vector2f
import pumpkin.math.vector.Vector3d

data class EntityNBT(
    val id: String,
    val onGround: Boolean,
    val air: Short,
    val fire: Short,
    val attackTime: Short,
    val deathTime: Short,
    val health: Short,
    val hurtTime: Short,
    val fallDistance: Float,
    val position: Vector3d,
    val velocity: Vector3d,
    val rotation: Vector2f
)