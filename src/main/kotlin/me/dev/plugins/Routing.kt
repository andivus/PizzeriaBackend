package me.dev.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.resources.*
import io.ktor.resources.*
import io.ktor.server.resources.Resources
import kotlinx.serialization.Serializable
import io.ktor.server.application.*
import me.dev.feature.item.resource.itemEndpoint
import me.dev.feature.order.resource.orderEndpoint

fun Application.configureRouting() {

    install(Resources)

    routing {

        route("/api"){

            get("/test") {
                call.respondText("Hello World!")
            }

            itemEndpoint()
            orderEndpoint()
        }

    }
}

