package pumpkin.world.id

import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue
import it.unimi.dsi.fastutil.ints.IntPriorityQueue

data class SimpleEntityIdManager(val worldServerId: Int) : EntityIdManager {
    init {
        check(worldServerId <= Byte.MAX_VALUE) { "Distributed Entity IDs can" }
    }

    companion object {
        const val COUNTER_MAX: Int = 16777216
    }

    private var counter: Int = 0
    private val reclaimed: IntPriorityQueue = IntArrayFIFOQueue()

    override fun acquire(): Int {
        if (!reclaimed.isEmpty) {
            return reclaimed.dequeueInt()
        }

        if (this.counter < COUNTER_MAX) {
            return (this.counter++).mask(this.worldServerId)
        }

        throw IllegalStateException("All entity IDs are in use!")
    }

    override fun release(id: Int) {
        val serverMask = id and 0xFF
        check(serverMask == worldServerId) { "Tried to release an entity id for a server ($serverMask) that isn't ours ($worldServerId)" }

        this.reclaimed.enqueue(id)
    }

    private fun Int.mask(worldServerId: Int): Int =
        (this shl 8) or (worldServerId and 0xFF)
}