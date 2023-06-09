package ua.vn.div.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.resources.*
import io.ktor.resources.*
import io.ktor.server.resources.Resources
import kotlinx.serialization.Serializable
import io.ktor.server.application.*
import ua.vn.div.feature.item.resource.itemEndpoint

fun Application.configureRouting() {

    install(Resources)

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        itemEndpoint()
    }
}

