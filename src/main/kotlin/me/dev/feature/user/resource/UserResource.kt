package me.dev.feature.user.resource

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.patch
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.dev.feature.user.data.User
import me.dev.feature.user.domain.UserRepository
import me.dev.feature.user.domain.model.*
import org.koin.ktor.ext.inject

@Resource("/users")
class Users {
    @Resource("{id}")
    class Id(val parent: Users, val id: String)

    @Resource("login")
    class Login(val parent: Users)

    @Resource("logout")
    class Logout(val parent: Users)

    @Resource("token")
    class Token(val parent: Users)

}

fun Route.userEndpoint() {
    val userRepository by inject<UserRepository>()

    post<Users.Login> {
        val request = call.receive<UserLoginRequest>()
        try {
            val result = userRepository.loginUser(request)
            if (result != null) call.respond(HttpStatusCode.OK, result)
            else call.respond(HttpStatusCode.Unauthorized)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    authenticate("token") {
        get<Users> {
            val result = userRepository.getAllUsers()
            call.respond(result)
        }

        get<Users.Id> {
            val result = userRepository.getUser(it.id)
            if (result != null) call.respond(HttpStatusCode.OK, result)
            else call.respond(HttpStatusCode.NotFound)
        }

        post<Users> {
            val request = call.receive<UserCreateRequest>()
            try {
                val result = userRepository.createUser(request)

                if (
                    result.status == UserCreateResponse.StatusType.USERNAME_ALREADY_IN_USE ||
                    result.status == UserCreateResponse.StatusType.EMAIL_ALREADY_IN_USE
                    )
                    call.respond(HttpStatusCode.BadRequest, result.status)

                else if (result.status != null) call.respond(HttpStatusCode.BadRequest, result.status)
                else if (result.userDTO != null) call.respond(HttpStatusCode.OK, result)

                else call.respond(HttpStatusCode.NotAcceptable)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        patch<Users.Id> {
            val request = call.receive<UserUpdateInfoRequest>()
            try {
                val result = userRepository.updateUserInfo(it.id, request)

                if (result != null) call.respond(HttpStatusCode.OK, result)

                else call.respond(HttpStatusCode.BadRequest)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        put<Users.Id> {
            val request = call.receive<UserUpdateCredRequest>()
            try {
                val result = userRepository.updateUserCred(it.id, request)

                if (result != null) call.respond(HttpStatusCode.OK, result)

                else call.respond(HttpStatusCode.BadRequest)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        delete<Users.Id> {
            try {
                val result = userRepository.removeUser(it.id)

                if (result != null) call.respond(HttpStatusCode.OK, result)

                else call.respond(HttpStatusCode.BadRequest)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        post<Users.Logout> {
            try {
                val token = this.context.request.authorization()!!

                val result = userRepository.logoutUser(token)

                call.respond(HttpStatusCode.OK, result)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        post<Users.Token> {
            try {
                val token = this.context.request.authorization()!!
                val result = userRepository.getTokenValidity(token)

                call.respond(HttpStatusCode.OK, result)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}