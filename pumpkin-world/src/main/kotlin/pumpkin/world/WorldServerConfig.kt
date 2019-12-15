package pumpkin.world

import kotlinx.serialization.Serializable
import pumpkin.protocol.modern.ModernProtocolVersion

@Serializable
data class WorldServerConfig(
    /**
     * The ticks per second that the world server should aim for.
     */
    val ticksPerSecond: Int = 20,
    /**
     * The minimum version required of clients to be able to join the world server.
     */
    @Serializable(with = ModernProtocolVersion.StringSerializer::class)
    val minimumVersion: ModernProtocolVersion = ModernProtocolVersion.Minecraft_1_14_4
) {

    /**
     * How long one tick lasts in milliseconds.
     */
    val tickDuration: Long get() = (1000 / this.ticksPerSecond).toLong()
}