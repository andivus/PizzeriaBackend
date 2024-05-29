package me.dev.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.resources.Resources
import io.ktor.server.application.*
import me.dev.feature.item.resource.itemEndpoint
import me.dev.feature.order.resource.orderEndpoint
import me.dev.feature.user.resource.userEndpoint

fun Application.configureRouting() {

    install(Resources)

    routing {

        route("/api"){

            get("/test") {
                call.respondText("Hello World!")
            }

            itemEndpoint()
            orderEndpoint()
            userEndpoint()
        }

    }
}

