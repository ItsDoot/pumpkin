package pumpkin.protocol.core

import kotlin.reflect.KClass

class ProtocolRegistry<out P : Packet>(val direction: Direction, supportedVersions: Collection<ProtocolVersion>) {
    init {
        require(supportedVersions.isNotEmpty()) { "supportedVersions must not be empty" }
    }

    /**
     * The protocol versions supported by this registry.
     */
    val supportedVersions: List<ProtocolVersion> = supportedVersions.sorted()

    /**
     * The minimum protocol version supported by this registry.
     */
    val minimumVersion: ProtocolVersion get() = this.supportedVersions.first()

    /**
     * The maximum protocol version supported by this registry.
     */
    val maximumVersion: ProtocolVersion get() = this.supportedVersions.last()

    private val versions = HashMap<ProtocolVersion, PacketCodecRegistry<P>>()

    init {
        for (version in this.supportedVersions) {
            this.versions[version] = PacketCodecRegistry(version)
        }
    }

    /**
     * Gets the codec registry that corresponds with the specified [version].
     */
    operator fun get(version: ProtocolVersion): PacketCodecRegistry<P> =
        requireNotNull(this.versions[version]) { "No registry found for protocol version $version" }

    /**
     * Registers the specified [codec] by the given packet id to protocol version mappings.
     */
    operator fun <T : @UnsafeVariance P> set(type: KClass<T>, vararg mappings: Pair<Int, ProtocolVersion>, codec: PacketCodec<T>) {
        require(mappings.isNotEmpty()) { "mappings must not be empty" }

        for (index in mappings.indices) {
            val (id, from) = mappings[index]
            val nextVersion = if (index + 1 < mappings.size) mappings[index + 1].second else from
            val to = if (from == nextVersion) this.maximumVersion else nextVersion

            require(from < to || from == this.maximumVersion) { "Next mapping version ($to) must be lower than the current ($from)" }

            for (version in supportedVersions.subList(from, to)) {
                if (version == to && nextVersion != from) {
                    break
                }

                val registry = requireNotNull(this.versions[version]) { "version $version is not supported by this registry" }

                require(id !in registry) { "A packet type with id $id is already registered for version $version" }

                registry[type, id] = codec
            }
        }
    }

    inline operator fun <reified T : @UnsafeVariance P> set(vararg mappings: Pair<Int, ProtocolVersion>, codec: PacketCodec<T>) {
        set(T::class, *mappings, codec = codec)
    }

    private fun <T : Comparable<T>> List<T>.subList(from: T, to: T): List<T> {
        val fromIndex = this.binarySearch(from)
        val toIndex = this.binarySearch(to, fromIndex = fromIndex + 1)
        return if (toIndex < 0) {
            this.subList(fromIndex, this.size)
        } else {
            this.subList(fromIndex, toIndex)
        }
    }
}