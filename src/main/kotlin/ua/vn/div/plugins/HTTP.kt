package ua.vn.div.plugins

import io.ktor.http.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        allowHeader(HttpHeaders.ContentType)
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    routing {
        options("{...}") {
            // Handle the preflight request
            // Set the appropriate CORS headers
            // For example:
            call.response.headers.append(HttpHeaders.AccessControlAllowOrigin, "http://localhost:4200")
            call.response.headers.append(HttpHeaders.AccessControlAllowHeaders, "*")
            call.response.headers.append(HttpHeaders.AccessControlAllowMethods, "*")
            call.response.headers.append(HttpHeaders.AccessControlAllowCredentials, "true")
            call.response.status(HttpStatusCode.OK)
        }
    }

    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }

    routing {
        openAPI(path = "openapi")
    }
    routing {
        swaggerUI(path = "openapi")
    }
}
