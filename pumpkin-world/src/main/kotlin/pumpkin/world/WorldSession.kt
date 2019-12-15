package pumpkin.world

import io.vertx.core.Vertx
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.kotlin.servicediscovery.serviceDiscoveryOptionsOf
import io.vertx.servicediscovery.ServiceDiscovery
import io.vertx.servicediscovery.types.MessageSource
import mu.KLogger
import mu.KotlinLogging
import pumpkin.auth.GameProfile
import pumpkin.event.Event
import pumpkin.util.logger
import io.vertx.kotlin.servicediscovery.types.MessageSource as KMessageSource

data class WorldSession(
    val vertx: Vertx,
    val server: WorldServer,
    val profile: GameProfile
) {
    companion object {
        @JvmField
        val LOGGER: KLogger = KotlinLogging.logger<WorldSession>()
    }

    lateinit var discovery: ServiceDiscovery

    lateinit var source: MessageConsumer<Event>

    suspend fun init() {
        this.discovery = ServiceDiscovery.create(this.vertx, serviceDiscoveryOptionsOf(name = "world-session"))

        this.source = KMessageSource.getConsumerAwait(this.discovery) {
            it.type == MessageSource.TYPE
                    && it.name == "network-session-source"
                    && it.metadata.getString("uuid") == profile.uuid.toString()
        }

        this.server.sessions += this

        LOGGER.info { "Player $profile joined world server" }
    }

    fun close() {
        this.source.unregister()
        this.discovery.close()

        this.server.sessions -= this
    }
}