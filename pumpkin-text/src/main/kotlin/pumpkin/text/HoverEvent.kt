package pumpkin.text

import kotlinx.serialization.Serializable

@Serializable
data class HoverEvent(val action: String, val value: Text) {

    companion object {
        @JvmStatic
        fun showText(text: Text): HoverEvent =
            HoverEvent("show_text", text)
    }
}