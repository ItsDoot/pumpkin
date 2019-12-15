package pumpkin.protocol.modern.type

import kotlinx.serialization.Serializable
import pumpkin.protocol.modern.ModernProtocolVersion
import pumpkin.text.LiteralText
import pumpkin.text.Text
import pumpkin.text.TextColor

@Serializable
data class PingResponse(
    val version: Version,
    val players: Players,
    val description: Text
) {
    companion object {
        val DEFAULT = PingResponse(
            version = Version(
                name = "1.14.4",
                protocol = 498
            ),
            players = Players(
                max = 100,
                online = 10,
                sample = emptyList()
            ),
            description = LiteralText(
                text = "A Pumpkin Server",
                color = TextColor.GREEN
            )
        )

        fun withVersion(version: ModernProtocolVersion): PingResponse = PingResponse(
            version = Version(
                name = version.name,
                protocol = version.protocol
            ),
            players = Players(
                max = 100,
                online = 10,
                sample = emptyList()
            ),
            description = LiteralText(
                text = "A Pumpkin Server",
                color = TextColor.GREEN
            )
        )
    }

    @Serializable
    data class Version(
        val name: String,
        val protocol: Int
    )

    @Serializable
    data class Players(
        val max: Int,
        val online: Int,
        val sample: List<Player>
    ) {

        @Serializable
        data class Player(
            val name: String,
            val id: String
        )
    }
}