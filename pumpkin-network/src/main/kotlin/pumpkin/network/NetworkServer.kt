package pumpkin.network

import io.vertx.core.net.NetServer
import io.vertx.kotlin.core.net.closeAwait
import io.vertx.kotlin.core.net.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.servicediscovery.serviceDiscoveryOptionsOf
import io.vertx.servicediscovery.ServiceDiscovery
import io.vertx.servicediscovery.types.MessageSource
import mu.KLogger
import mu.KotlinLogging
import pumpkin.util.logger

class NetworkServer : CoroutineVerticle() {
    companion object {
        @JvmField
        val LOGGER: KLogger = KotlinLogging.logger<NetworkServer>()
    }

    lateinit var netServer: NetServer
        private set

    val id: String get() = deploymentID

    val sessions = HashSet<NetworkSession>()

    val discovery by lazy {
        ServiceDiscovery.create(vertx, serviceDiscoveryOptionsOf(name = "network-server"))
    }

    override suspend fun start() {
        this.netServer = vertx.createNetServer()
            .connectHandler { socket ->
                socket.writeHandlerID()
                val session = NetworkSession(vertx, this, socket)
                LOGGER.info("Connection established from ${socket.remoteAddress()}")

                val eventSource =
                    MessageSource.createRecord("network-session-source", "network.event.${socket.writeHandlerID()}")

                socket.handler(session::receive)
                    .closeHandler {
                        LOGGER.info("Connection closed from ${socket.remoteAddress()}")
                        session.close()
                        this.sessions -= session
                    }
                    .exceptionHandler {
                        it.printStackTrace()
                        socket.close()
                    }

                this.sessions += session
            }

        this.netServer.listenAwait(25565)
        LOGGER.info("Listening on port 25565.")
    }

    override suspend fun stop() {
        this.netServer.closeAwait()
        this.sessions.clear()
    }
}