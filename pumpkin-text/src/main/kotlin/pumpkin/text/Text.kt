package pumpkin.text

import kotlinx.serialization.CompositeDecoder.Companion.READ_DONE
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.list
import pumpkin.text.serialization.FormattingCodeSerializer
import pumpkin.util.serialization.Formats

@Serializable(Text.Companion::class)
sealed class Text {

    abstract val bold: Boolean

    abstract val italic: Boolean

    abstract val underlined: Boolean

    abstract val strikethrough: Boolean

    abstract val obfuscated: Boolean

    abstract val color: TextColor?

    abstract val insertion: String?

    abstract val clickEvent: ClickEvent?

    abstract val hoverEvent: HoverEvent?

    abstract val extra: List<Text>

    open fun toJson(): JsonElement = Formats.jsonNoDefaults.toJson(serializer(), this)

    open fun toJsonString(): String = Formats.jsonNoDefaults.stringify(serializer(), this)

    open fun toLegacy(): String =
        throw UnsupportedOperationException("This text doesn't support legacy serialization")

    @Serializer(forClass = Text::class)
    companion object : KSerializer<Text> {
        fun fromJson(value: JsonElement): Text = Formats.jsonNoDefaults.fromJson(serializer(), value)

        fun fromJsonString(value: String): Text = Formats.jsonNoDefaults.parse(serializer(), value)

        override val descriptor: SerialDescriptor = object : SerialClassDescImpl("Text") {
            init {
                addElement("text", isOptional = true)

                addElement("translate", isOptional = true)
                addElement("with", isOptional = true)

                addElement("bold", isOptional = true)
                addElement("italic", isOptional = true)
                addElement("underlined", isOptional = true)
                addElement("strikethrough", isOptional = true)
                addElement("obfuscated", isOptional = true)
                addElement("color", isOptional = true)
                addElement("insertion", isOptional = true)
                addElement("clickEvent", isOptional = true)
                addElement("hoverEvent", isOptional = true)
                addElement("extra", isOptional = true)
            }
        }

        override fun serialize(encoder: Encoder, obj: Text) {
            val composite = encoder.beginStructure(this.descriptor)
            when (obj) {
                is LiteralText -> {
                    composite.encodeStringElement(this.descriptor, 0, obj.text)
                }
                is TranslationText -> {
                    composite.encodeStringElement(this.descriptor, 1, obj.translate)
                    composite.encodeSerializableElement(this.descriptor, 2, serializer().list, obj.with)
                }
            }

            if (obj.bold) composite.encodeBooleanElement(this.descriptor, 3, obj.bold)
            if (obj.italic) composite.encodeBooleanElement(this.descriptor, 4, obj.italic)
            if (obj.underlined) composite.encodeBooleanElement(this.descriptor, 5, obj.underlined)
            if (obj.strikethrough) composite.encodeBooleanElement(this.descriptor, 6, obj.strikethrough)
            if (obj.obfuscated) composite.encodeBooleanElement(this.descriptor, 7, obj.obfuscated)

            obj.color?.let { composite.encodeSerializableElement(this.descriptor, 8, TextColor, it) }
            obj.insertion?.let { composite.encodeStringElement(this.descriptor, 9, it) }
            obj.clickEvent?.let { composite.encodeSerializableElement(this.descriptor, 10, ClickEvent.serializer(), it) }
            obj.hoverEvent?.let { composite.encodeSerializableElement(this.descriptor, 11, HoverEvent.serializer(), it) }

            if (obj.extra.isNotEmpty())
                composite.encodeSerializableElement(this.descriptor, 12, serializer().list, obj.extra)

            composite.endStructure(this.descriptor)
        }

        override fun deserialize(decoder: Decoder): Text {
            val composite = decoder.beginStructure(this.descriptor)

            var text: String? = null

            var translate: String? = null
            var with: List<Text> = emptyList()

            var bold: Boolean = false
            var italic: Boolean = false
            var underlined: Boolean = false
            var strikethrough: Boolean = false
            var obfuscated: Boolean = false
            var color: TextColor? = null
            var insertion: String? = null
            var clickEvent: ClickEvent? = null
            var hoverEvent: HoverEvent? = null
            var extra: List<Text> = emptyList()

            loop@ while (true) {
                when (val i = composite.decodeElementIndex(this.descriptor)) {
                    READ_DONE -> break@loop
                    0 -> text = composite.decodeStringElement(this.descriptor, i)
                    1 -> translate = composite.decodeStringElement(this.descriptor, i)
                    2 -> with = composite.decodeSerializableElement(this.descriptor, i, serializer().list)
                    3 -> bold = composite.decodeBooleanElement(this.descriptor, i)
                    4 -> italic = composite.decodeBooleanElement(this.descriptor, i)
                    5 -> underlined = composite.decodeBooleanElement(this.descriptor, i)
                    6 -> strikethrough = composite.decodeBooleanElement(this.descriptor, i)
                    7 -> obfuscated = composite.decodeBooleanElement(this.descriptor, i)
                    8 -> color = composite.decodeSerializableElement(this.descriptor, i, TextColor)
                    9 -> insertion = composite.decodeStringElement(this.descriptor, i)
                    10 -> clickEvent = composite.decodeSerializableElement(this.descriptor, i, ClickEvent.serializer())
                    11 -> hoverEvent = composite.decodeSerializableElement(this.descriptor, i, HoverEvent.serializer())
                    12 -> extra = composite.decodeSerializableElement(this.descriptor, i, serializer().list)
                    else -> throw SerializationException("Unknown index $i")
                }
            }

            composite.endStructure(this.descriptor)

            if (text != null) {
                return LiteralText(
                    text,
                    bold, italic, underlined, strikethrough, obfuscated,
                    color, insertion, clickEvent, hoverEvent, extra
                )
            }
            if (translate != null) {
                return TranslationText(
                    translate, with,
                    bold, italic, underlined, strikethrough, obfuscated,
                    color, insertion, clickEvent, hoverEvent, extra
                )
            }

            throw SerializationException("Decoded text must either have a 'text' or 'translate' field!")
        }
    }
}

@Serializable
data class LiteralText(
    val text: String,
    override val bold: Boolean = false,
    override val italic: Boolean = false,
    override val underlined: Boolean = false,
    override val strikethrough: Boolean = false,
    override val obfuscated: Boolean = false,
    override val color: TextColor? = null,
    override val insertion: String? = null,
    override val clickEvent: ClickEvent? = null,
    override val hoverEvent: HoverEvent? = null,
    override val extra: List<Text> = emptyList()
) : Text() {
    override fun toJson(): JsonElement = Formats.jsonNoDefaults.toJson(serializer(), this)

    override fun toJsonString(): String = Formats.jsonNoDefaults.stringify(serializer(), this)

    override fun toLegacy(): String = FormattingCodeSerializer.LEGACY.serialize(this)
}

@Serializable
data class TranslationText(
    val translate: String,
    val with: List<Text> = emptyList(),
    override val bold: Boolean = false,
    override val italic: Boolean = false,
    override val underlined: Boolean = false,
    override val strikethrough: Boolean = false,
    override val obfuscated: Boolean = false,
    override val color: TextColor? = null,
    override val insertion: String? = null,
    override val clickEvent: ClickEvent? = null,
    override val hoverEvent: HoverEvent? = null,
    override val extra: List<Text> = emptyList()
) : Text() {
    override fun toJson(): JsonElement = Formats.jsonNoDefaults.toJson(serializer(), this)

    override fun toJsonString(): String = Formats.jsonNoDefaults.stringify(serializer(), this)
}