package pumpkin.text

import kotlinx.serialization.Serializable

@Serializable
data class ClickEvent(val action: String, val value: String) {

    companion object {
        @JvmStatic
        fun openUrl(url: String): ClickEvent =
            ClickEvent("open_url", url)

        @JvmStatic
        fun runCommand(command: String): ClickEvent =
            ClickEvent("run_command", command)

        @JvmStatic
        fun suggestCommand(command: String): ClickEvent =
            ClickEvent("suggest_command", command)

        @JvmStatic
        fun changePage(page: Int): ClickEvent =
            ClickEvent("change_page", page.toString())
    }
}