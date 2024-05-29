package me.dev.feature.user.domain.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import me.dev.feature.order.domain.model.OrderDTO

@Serializable
data class UserDTO(
    val uuid: String,
    val username: String,
    val firstName: String,
    val secondName: String,
    val email: String
)

@Serializable
data class UserCreateRequest(
    val username: String,
    val firstName: String,
    val secondName: String,
    val email: String,
    val password: String
)

@Serializable
data class UserCreateResponse(
    val userDTO: UserDTO? = null,
    val status: StatusType? = null
) {
    enum class StatusType {
        USERNAME_ALREADY_IN_USE,
        EMAIL_ALREADY_IN_USE,
        BAD_USERNAME,
        BAD_MAIL,
        BAD_PASSWORD,
        BAD_NAME
    }
}

@Serializable
data class UserUpdateInfoRequest(
    val username: String,
    val firstName: String,
    val secondName: String,
    val email: String,
)

@Serializable
data class UserUpdateCredRequest(
    val password: String
)

@Serializable
data class UserLoginRequest(
    val login: String,
    val password: String
)

@Serializable
data class UserLoginResponse(
    val token: String
)

@Serializable
data class GetTokenValidityResponse(
    val validTime: LocalDateTime
)