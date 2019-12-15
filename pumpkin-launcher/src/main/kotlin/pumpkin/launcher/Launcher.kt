package pumpkin.launcher

import io.vertx.core.Vertx
import io.vertx.ext.shell.ShellService
import io.vertx.kotlin.ext.shell.shellServiceOptionsOf
import io.vertx.kotlin.ext.shell.startAwait
import io.vertx.kotlin.ext.shell.term.httpTermOptionsOf
import io.vertx.kotlin.servicediscovery.getRecordAwait
import io.vertx.kotlin.servicediscovery.publishAwait
import io.vertx.kotlin.servicediscovery.recordOf
import io.vertx.servicediscovery.ServiceDiscovery
import kotlinx.coroutines.runBlocking
import mu.KLogger
import mu.KotlinLogging
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pumpkin.network.NetworkServer

object Launcher {
    private val LOGGER: Logger = LoggerFactory.getLogger("Launcher")
    private val SHELL_LOGGER: KLogger = KotlinLogging.logger("Shell")

    @JvmStatic
    fun main(args: Array<String>) = runBlocking<Unit> {
        val vertx = Vertx.vertx()

        LOGGER.info("Launching shell service...")
        SHELL_LOGGER.info("HTTP enabled at 127.0.0.1:80")
        val shellOptions = shellServiceOptionsOf(
            httpOptions = httpTermOptionsOf(
                clientAuthRequired = false,
                host = "localhost",
                port = 8080
            ),
            welcomeMessage = "Pumpkin v0.1.0 - A Distributed Minecraft Server Platform"
        )
        ShellService.create(vertx, shellOptions).startAwait()
        LOGGER.info("Shell service started.")

        LOGGER.info("Starting network server...")
        val network = NetworkServer()
        vertx.deployVerticle(network)

        val discovery: ServiceDiscovery = ServiceDiscovery.create(vertx)
        discovery.publishAwait(recordOf(name = "0", type = "world-server"))
        LOGGER.info("WorldServer published.")

        val record = discovery.getRecordAwait { it.name == "0" }
        println("Record? $record")
    }
}