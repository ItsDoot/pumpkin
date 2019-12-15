package pumpkin.protocol.core

import kotlin.reflect.KClass

class PacketCodecRegistry<out P : Packet>(val version: ProtocolVersion) {

    private val idToTypeMap = HashMap<KClass<out P>, Int>()
    private val codecs = HashMap<Int, PacketCodec<P>>()

    operator fun get(id: Int): PacketCodec<@UnsafeVariance P> = requireNotNull(this.codecs[id]) { "Unknown packet id: 0x${id.toString(16)}" }

    operator fun get(type: KClass<out @UnsafeVariance P>): PacketCodec<@UnsafeVariance P> = this[getId(type)]

    fun getId(type: KClass<out @UnsafeVariance P>): Int = requireNotNull(this.idToTypeMap[type]) { "Unknown packet type: $type" }

    @Suppress("UNCHECKED_CAST")
    operator fun <T : @UnsafeVariance P> set(type: KClass<T>, id: Int, codec: PacketCodec<T>) {
        this.idToTypeMap[type] = id
        this.codecs[id] = codec as PacketCodec<P>
    }

    inline operator fun <reified T : @UnsafeVariance P> set(id: Int, codec: PacketCodec<T>) {
        this[T::class, id] = codec
    }

    operator fun contains(id: Int): Boolean = id in this.codecs
}