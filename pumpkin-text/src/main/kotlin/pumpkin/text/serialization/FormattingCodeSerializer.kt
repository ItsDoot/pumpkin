package pumpkin.text.serialization

import pumpkin.text.LiteralText

data class FormattingCodeSerializer(val code: Char) : TextSerializer<LiteralText, String, String> {

    companion object {
        @JvmField
        val LEGACY = FormattingCodeSerializer('\u00a7')

        @JvmField
        val AND = FormattingCodeSerializer('&')
    }

    private fun serialize(text: LiteralText, builder: StringBuilder) {
        if (text.color != null) builder.append("$code${text.color.code}")
        if (text.bold) builder.append("${code}L")
        if (text.italic) builder.append("${code}O")
        if (text.underlined) builder.append("${code}N")
        if (text.strikethrough) builder.append("${code}M")
        if (text.obfuscated) builder.append("${code}K")

        builder.append(text.text)

        for (child in text.extra) {
            require(child is LiteralText) { "FormattingCodeSerializer can only serialize LiteralTexts" }
            serialize(child, builder)
        }
    }

    override fun serialize(text: LiteralText): String =
        StringBuilder().apply { serialize(text, this) }.toString()

    override fun deserialize(input: String): LiteralText {
        TODO()
    }
}