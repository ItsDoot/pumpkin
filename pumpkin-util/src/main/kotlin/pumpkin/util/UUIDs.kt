package pumpkin.util

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.toUtf8Bytes
import kotlinx.serialization.withName
import java.util.UUID

object UUIDs {

    fun offline(username: String): UUID =
        UUID.nameUUIDFromBytes("OfflinePlayer:$username".toUtf8Bytes())

    @Serializer(UUID::class)
    object StringSerializer : KSerializer<UUID> {
        override val descriptor: SerialDescriptor = StringDescriptor.withName("UUID")

        override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())

        override fun serialize(encoder: Encoder, obj: UUID) = encoder.encodeString(obj.toString())
    }
}