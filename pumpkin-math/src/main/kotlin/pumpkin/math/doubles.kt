package pumpkin.math

fun Double.floorToInt(): Int {
    val truncated = this.toInt()
    if (this < truncated) {
        return truncated - 1
    }
    return truncated
}