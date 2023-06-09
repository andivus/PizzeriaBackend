package ua.vn.div

import io.ktor.server.application.*
import org.koin.core.module.Module
import ru.somniumcraft.plugins.configureKoin
import ua.vn.div.configuration.setupConfig
import ua.vn.div.dependencyInjection.appModule
import ua.vn.div.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module(testing: Boolean = false, koinModules: List<Module> = listOf(appModule)) {
    configureKoin(koinModules)
    setupConfig()
    configureDatabase()
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()
}
