package pumpkin.world

import io.vertx.core.eventbus.MessageConsumer
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.servicediscovery.serviceDiscoveryOptionsOf
import io.vertx.servicediscovery.ServiceDiscovery
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.slf4j.Logger
import pumpkin.auth.GameProfile
import pumpkin.util.VertxDispatcher
import pumpkin.util.logger
import pumpkin.util.serialization.Formats

class WorldServer : CoroutineVerticle() {
    companion object {
        val logger: Logger = KotlinLogging.logger<WorldServer>()
    }

    /**
     * Whether this world server is loaded.
     */
    private var loaded: Boolean = true

    /**
     * The world server config.
     */
    private val worldConfig: WorldServerConfig by lazy {
        Formats.json.parse(WorldServerConfig.serializer(), config.encode())
    }

    internal val sessions = HashSet<WorldSession>()

    private val discovery by lazy {
        ServiceDiscovery.create(vertx, serviceDiscoveryOptionsOf(name = "world-server"))
    }

    private lateinit var joinConsumer: MessageConsumer<GameProfile>

    override suspend fun start() {
        this.joinConsumer = vertx.eventBus().consumer<GameProfile>("pumpkin.world.$deploymentID.join")
            .handler { message ->
                val profile = message.body()
                    ?: throw IllegalStateException("world-server $deploymentID received a null join message")

                val session = WorldSession(this.vertx, this, profile)

                GlobalScope.launch(VertxDispatcher) {
                    session.init()

                    // In the future this will reply false when the client isn't allowed to connect.
                    // Such as server being too full or whitelisted, user banned, etc.
                    message.reply(true)
                }
            }

        vertx.setPeriodic(this.worldConfig.tickDuration) {
            if (!this.loaded) {
                vertx.cancelTimer(it)
                return@setPeriodic
            }

            tick()
        }
    }

    override suspend fun stop() {
        this.joinConsumer.unregister()
    }

    private fun tick() {

    }
}