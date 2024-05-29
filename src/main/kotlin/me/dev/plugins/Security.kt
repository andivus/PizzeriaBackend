package me.dev.plugins

import io.ktor.server.sessions.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import me.dev.common.seed.SeedService
import me.dev.feature.user.data.UserTokens
import me.dev.feature.user.data.UserTokensTable
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.ktor.ext.inject
import java.time.LocalDateTime

fun Application.configureSecurity() {
    data class AppPrincipal(val key: String) : Principal

    authentication {
        bearer("token") {
            authenticate{tokenCredential ->
                val validTime = newSuspendedTransaction {
                    return@newSuspendedTransaction UserTokens.find { UserTokensTable.token eq tokenCredential.token }.firstOrNull()?.validTime
                }

                if(validTime != null && validTime < LocalDateTime.now().plusDays(90)){
                    AppPrincipal(tokenCredential.token)
                } else{
                    null
                }
            }
        }
    }

    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
    routing {
        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }
    }
}
