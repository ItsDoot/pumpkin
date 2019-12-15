package pumpkin.text

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor

@Serializable(TextColor.Companion::class)
enum class TextColor(val code: Char) {
    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    GREEN('A'),
    AQUA('B'),
    RED('C'),
    LIGHT_PURPLE('D'),
    YELLOW('E'),
    WHITE('F'),
    RESET('R');

    val jsonName: String = name.toLowerCase()

    fun toFormattingCode(char: Char): String = "$char$code"

    fun toLegacy(): String = "\u00a7$code"

    @Serializer(TextColor::class)
    companion object : KSerializer<TextColor> {
        override val descriptor: SerialDescriptor get() = StringDescriptor.withName("TextColor")

        private val jsonNameMap: Map<String, TextColor> = values().associateBy { it.jsonName }

        override fun serialize(encoder: Encoder, obj: TextColor) {
            encoder.encodeString(obj.jsonName)
        }

        override fun deserialize(decoder: Decoder): TextColor {
            val name = decoder.decodeString()
            return jsonNameMap[name] ?: throw NoSuchElementException(name)
        }
    }
}