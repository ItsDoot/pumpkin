package pumpkin.world.id

interface EntityIdManager {

    fun acquire(): Int

    fun acquire(count: Int): IntArray {
        val result = IntArray(count)
        repeat(count) {
            result[it] = acquire()
        }
        return result
    }

    fun release(id: Int)

    fun release(ids: IntArray) {
        ids.forEach(this::release)
    }
}