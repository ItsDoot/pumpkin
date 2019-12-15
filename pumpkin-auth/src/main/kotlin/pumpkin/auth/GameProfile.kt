package pumpkin.auth

import kotlinx.serialization.Serializable
import pumpkin.util.UUIDs
import java.util.UUID

@Serializable
data class GameProfile(
    @Serializable(UUIDs.StringSerializer::class)
    val uuid: UUID,
    val username: String
)