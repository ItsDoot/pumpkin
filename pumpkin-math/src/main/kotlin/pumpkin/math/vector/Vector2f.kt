package pumpkin.math.vector

import kotlinx.serialization.Serializable
import pumpkin.math.Constants
import pumpkin.math.floorToInt
import kotlin.math.*

@Serializable
data class Vector2f(val x: Double, val y: Double) : Comparable<Vector2f> {
    companion object {
        val ZERO = Vector2f(0.0, 0.0)
        val UNIT_X = Vector2f(1.0, 0.0)
        val UNIT_Y = Vector2f(0.0, 1.0)
        val ONE = Vector2f(1.0, 1.0)
        val RIGHT: Vector2f = UNIT_X
        val UP: Vector2f = UNIT_Y

        operator fun invoke(): Vector2f = ZERO
    }

    val floorX: Int get() = x.floorToInt()

    val floorY: Int get() = y.floorToInt()

    operator fun plus(other: Vector2f): Vector2f =
        Vector2f(this.x + other.x, this.y + other.y)

    operator fun minus(other: Vector2f): Vector2f =
        Vector2f(this.x - other.x, this.y - other.y)

    operator fun times(multiplier: Double): Vector2f =
        Vector2f(this.x * multiplier, this.y * multiplier)

    operator fun times(other: Vector2f): Vector2f =
        Vector2f(this.x * other.x, this.y * other.y)

    operator fun div(divisor: Double): Vector2f =
        Vector2f(this.x / divisor, this.y / divisor)

    operator fun div(other: Vector2f): Vector2f =
        Vector2f(this.x / other.x, this.y / other.y)

    fun dot(other: Vector2f): Double =
        this.x * other.x + this.y * other.y

    fun pow(power: Double): Vector2f =
        Vector2f(this.x.pow(power), this.y.pow(power))

    fun ceil(): Vector2f =
        Vector2f(ceil(this.x), ceil(this.y))

    fun floor(): Vector2f =
        Vector2f(floor(this.x), floor(this.y))

    fun round(): Vector2f =
        Vector2f(round(this.x), round(this.y))

    operator fun unaryPlus(): Vector2f =
        Vector2f(abs(this.x), abs(this.y))

    operator fun unaryMinus(): Vector2f =
        Vector2f(-this.x, -this.y)

    fun min(other: Vector2f): Vector2f =
        Vector2f(min(this.x, other.x), min(this.y, other.y))

    fun max(other: Vector2f): Vector2f =
        Vector2f(max(this.x, other.x), max(this.y, other.y))

    fun distanceSquared(other: Vector2f): Double {
        val dx = this.x - other.x
        val dy = this.y - other.y
        return dx * dx + dy * dy
    }

    fun distance(other: Vector2f): Double =
        sqrt(distanceSquared(other))

    fun lengthSquared(): Double =
        x * x + y * y

    fun length(): Double =
        sqrt(lengthSquared())

    fun normalize(): Vector2f {
        val length = length()
        if (length.absoluteValue < Constants.DBL_EPSILON) {
            throw ArithmeticException("Cannot normalize the zero vector")
        }
        return Vector2f(x / length, y / length)
    }

    override fun compareTo(other: Vector2f): Int =
        sign(this.lengthSquared() - other.lengthSquared()).toInt()
}