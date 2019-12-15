package pumpkin.protocol.modern.type

import io.netty.buffer.ByteBuf
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.IntDescriptor
import kotlinx.serialization.withName

@Serializable(ChatType.ChatTypeSerializer::class)
enum class ChatType(val id: Int) {
    CHAT(0),
    SYSTEM(1),
    ACTION_BAR(2);

    companion object {
        private val FROM_ID =
            values().associateBy(ChatType::id)

        operator fun get(id: Int): ChatType =
            requireNotNull(FROM_ID[id]) { "Unknown chat type: $id" }
    }

    object ChatTypeSerializer : KSerializer<ChatType> {
        override val descriptor: SerialDescriptor = IntDescriptor.withName("ChatType")

        override fun deserialize(decoder: Decoder): ChatType =
            ChatType[decoder.decodeByte().toInt()]

        override fun serialize(encoder: Encoder, obj: ChatType) =
            encoder.encodeByte(obj.id.toByte())
    }
}

fun ByteBuf.readChatType(): ChatType =
    ChatType[this.readByte().toInt()]

fun ByteBuf.writeChatType(value: ChatType): ByteBuf =
    this.writeByte(value.id)