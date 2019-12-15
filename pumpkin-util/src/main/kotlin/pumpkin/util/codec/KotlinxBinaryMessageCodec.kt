package pumpkin.util.codec

import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.KSerializer
import kotlinx.serialization.cbor.Cbor
import kotlin.reflect.KClass

class KotlinxBinaryMessageCodec<T : Any>(
    private val format: BinaryFormat,
    private val baseType: KClass<T>
) : MessageCodec<T, T> {

    override fun systemCodecID(): Byte = -1

    override fun name(): String = "KotlinxBinaryMessageCodec"

    override fun encodeToWire(buffer: Buffer, s: T) {
        val serializer: KSerializer<out T> = requireNotNull(format.context.getPolymorphic(baseType, s)) {
            "No polymorphic serializer found for sub-type ${s::class} of base-type $baseType"
        }
        val bytes = format.dump(serializer as KSerializer<T>, s)
        buffer.appendInt(bytes.size).appendBytes(bytes)
    }

    override fun decodeFromWire(pos: Int, buffer: Buffer): T {
        val typeLength = buffer.getInt(pos)
        val eventType = buffer.getString(pos + 4, pos + 4 + typeLength)
        val serializer: KSerializer<out T> = requireNotNull(format.context.getPolymorphic(baseType, eventType)) {
            "No polymorphic serializer found for sub-type $eventType of base-type $baseType"
        }

        val numBytes = buffer.getInt(pos + 4 + typeLength)
        val bytes = buffer.getBytes(pos + 4 + typeLength + 4, pos + 4 + typeLength + 4 + numBytes)
        return format.load(serializer as KSerializer<T>, bytes)
    }

    override fun transform(s: T): T = s

    companion object {
        inline fun <reified T : Any> cbor(): MessageCodec<T, T> =
            KotlinxBinaryMessageCodec(Cbor.plain, T::class)
    }
}