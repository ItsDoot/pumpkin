package pumpkin.math.vector

import kotlinx.serialization.Serializable
import pumpkin.math.Constants
import pumpkin.math.floorToInt
import kotlin.math.*

@Serializable
data class Vector3d(val x: Double, val y: Double, val z: Double) : Comparable<Vector3d> {
    companion object {
        val ZERO = Vector3d(0.0, 0.0, 0.0)
        val UNIT_X = Vector3d(1.0, 0.0, 0.0)
        val UNIT_Y = Vector3d(0.0, 1.0, 0.0)
        val UNIT_Z = Vector3d(0.0, 0.0, 1.0)
        val ONE = Vector3d(1.0, 1.0, 1.0)
        val RIGHT: Vector3d = UNIT_X
        val UP: Vector3d = UNIT_Y
        val FORWARD: Vector3d = UNIT_Z

        operator fun invoke(): Vector3d = ZERO
    }

    val floorX: Int get() = x.floorToInt()

    val floorY: Int get() = y.floorToInt()

    val floorZ: Int get() = z.floorToInt()

    operator fun plus(other: Vector3d): Vector3d =
        Vector3d(this.x + other.x, this.y + other.y, this.z + other.z)

    operator fun minus(other: Vector3d): Vector3d =
        Vector3d(this.x - other.x, this.y - other.y, this.z - other.z)

    operator fun times(multiplier: Double): Vector3d =
        Vector3d(this.x * multiplier, this.y * multiplier, this.z * multiplier)

    operator fun times(other: Vector3d): Vector3d =
        Vector3d(this.x * other.x, this.y * other.y, this.z * other.z)

    operator fun div(divisor: Double): Vector3d =
        Vector3d(this.x / divisor, this.y / divisor, this.z / divisor)

    operator fun div(other: Vector3d): Vector3d =
        Vector3d(this.x / other.x, this.y / other.y, this.z / other.z)

    fun dot(other: Vector3d): Double =
        this.x * other.x + this.y * other.y + this.z * other.z

    fun cross(other: Vector3d): Vector3d =
        Vector3d(
            this.y * other.z - this.z * other.y,
            this.z * other.x - this.x * other.z,
            this.x * other.y - this.y * other.x
        )

    fun pow(power: Double): Vector3d =
        Vector3d(this.x.pow(power), this.y.pow(power), this.z.pow(power))

    fun ceil(): Vector3d =
        Vector3d(ceil(this.x), ceil(this.y), ceil(this.z))

    fun floor(): Vector3d =
        Vector3d(floor(this.x), floor(this.y), floor(this.z))

    fun round(): Vector3d =
        Vector3d(round(this.x), round(this.y), round(this.z))

    operator fun unaryPlus(): Vector3d =
        Vector3d(abs(this.x), abs(this.y), abs(this.z))

    operator fun unaryMinus(): Vector3d =
        Vector3d(-this.x, -this.y, -this.z)

    fun min(other: Vector3d): Vector3d =
        Vector3d(min(this.x, other.x), min(this.y, other.y), min(this.z, other.z))

    fun max(other: Vector3d): Vector3d =
        Vector3d(max(this.x, other.x), max(this.y, other.y), max(this.z, other.z))

    fun distanceSquared(other: Vector3d): Double {
        val dx = this.x - other.x
        val dy = this.y - other.y
        val dz = this.z - other.z
        return dx * dx + dy * dy + dz * dz
    }

    fun distance(other: Vector3d): Double =
        sqrt(distanceSquared(other))

    fun lengthSquared(): Double =
        x * x + y * y + z * z

    fun length(): Double =
        sqrt(lengthSquared())

    fun normalize(): Vector3d {
        val length = length()
        if (length.absoluteValue < Constants.DBL_EPSILON) {
            throw ArithmeticException("Cannot normalize the zero vector")
        }
        return Vector3d(x / length, y / length, z / length)
    }

    override fun compareTo(other: Vector3d): Int =
        sign(this.lengthSquared() - other.lengthSquared()).toInt()
}