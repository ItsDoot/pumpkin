package pumpkin.math.vector

import kotlinx.serialization.Serializable
import kotlin.math.*

@Serializable
data class Vector2i(val x: Int, val y: Int) : Comparable<Vector2i> {
    companion object {
        val ZERO = Vector2i(0, 0)
        val UNIT_X = Vector2i(1, 0)
        val UNIT_Y = Vector2i(0, 1)
        val ONE = Vector2i(1, 1)
        val RIGHT: Vector2i = UNIT_X
        val UP: Vector2i = UNIT_Y

        operator fun invoke(): Vector2i = ZERO
    }

    operator fun plus(other: Vector2i): Vector2i =
        Vector2i(this.x + other.x, this.y + other.y)

    operator fun minus(other: Vector2i): Vector2i =
        Vector2i(this.x - other.x, this.y - other.y)

    operator fun times(multiplier: Int): Vector2i =
        Vector2i(this.x * multiplier, this.y * multiplier)

    operator fun times(other: Vector2i): Vector2i =
        Vector2i(this.x * other.x, this.y * other.y)

    operator fun div(divisor: Int): Vector2i =
        Vector2i(this.x / divisor, this.y / divisor)

    operator fun div(other: Vector2i): Vector2i =
        Vector2i(this.x / other.x, this.y / other.y)

    fun dot(other: Vector2i): Int =
        this.x * other.x + this.y * other.y

    fun pow(power: Double): Vector2i =
        Vector2i(this.x.toDouble().pow(power).toInt(), this.y.toDouble().pow(power).toInt())

    fun pow(power: Int): Vector2i =
        Vector2i(this.x.toDouble().pow(power).toInt(), this.y.toDouble().pow(power).toInt())

    operator fun unaryPlus(): Vector2i =
        Vector2i(abs(this.x), abs(this.y))

    operator fun unaryMinus(): Vector2i =
        Vector2i(-this.x, -this.y)

    fun min(other: Vector2i): Vector2i =
        Vector2i(min(this.x, other.x), min(this.y, other.y))

    fun max(other: Vector2i): Vector2i =
        Vector2i(max(this.x, other.x), max(this.y, other.y))

    fun distanceSquared(other: Vector2i): Int {
        val dx = this.x - other.x
        val dy = this.y - other.y
        return dx * dx + dy * dy
    }

    fun distance(other: Vector2i): Float =
        sqrt(distanceSquared(other).toDouble()).toFloat()

    fun lengthSquared(): Int =
        x * x + y * y

    fun length(): Float =
        sqrt(lengthSquared().toDouble()).toFloat()

    override fun compareTo(other: Vector2i): Int =
        this.lengthSquared() - other.lengthSquared()
}