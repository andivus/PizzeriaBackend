package me.dev.feature.user.domain

import kotlinx.datetime.toKotlinLocalDateTime
import me.dev.common.password.PasswordService
import me.dev.common.token.TokenService
import me.dev.common.username.UserDataValidatorService
import me.dev.feature.user.data.User
import me.dev.feature.user.data.UserTable
import me.dev.feature.user.data.UserTokens
import me.dev.feature.user.data.UserTokensTable
import me.dev.feature.user.domain.mapper.toDTO
import me.dev.feature.user.domain.model.*
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDateTime
import java.util.*

class UserRepositoryImpl: UserRepository, KoinComponent {
    private val passwordService: PasswordService by inject()
    private val userDataValidatorService: UserDataValidatorService by inject()
    private val tokenService: TokenService by inject()

    override suspend fun getAllUsers(): List<UserDTO> {
        val users = newSuspendedTransaction {
            return@newSuspendedTransaction User.all().sortedBy { i -> i.firstName }.toList()
        }
        return users.map { u -> u.toDTO() }
    }

    override suspend fun getUser(uuid: String): UserDTO? {
        return newSuspendedTransaction {
            return@newSuspendedTransaction User.findById(UUID.fromString(uuid))?.toDTO()
        }
    }

    override suspend fun createUser(request: UserCreateRequest): UserCreateResponse {
        return newSuspendedTransaction {

            if (!User.find { UserTable.username.lowerCase() eq request.username.lowercase() }.empty())
                return@newSuspendedTransaction UserCreateResponse(status = UserCreateResponse.StatusType.USERNAME_ALREADY_IN_USE)

            if (!User.find { UserTable.email.lowerCase() eq request.email.lowercase() }.empty())
                return@newSuspendedTransaction UserCreateResponse(status = UserCreateResponse.StatusType.EMAIL_ALREADY_IN_USE)

            if (!passwordService.validatePassword(request.password))
                return@newSuspendedTransaction UserCreateResponse(status = UserCreateResponse.StatusType.BAD_PASSWORD)

            if (!userDataValidatorService.validateUsername(request.username))
                return@newSuspendedTransaction UserCreateResponse(status = UserCreateResponse.StatusType.BAD_USERNAME)

            if (!userDataValidatorService.validateFirstOrSecondName(request.firstName))
                return@newSuspendedTransaction UserCreateResponse(status = UserCreateResponse.StatusType.BAD_NAME)

            if (!userDataValidatorService.validateFirstOrSecondName(request.secondName))
                return@newSuspendedTransaction UserCreateResponse(status = UserCreateResponse.StatusType.BAD_NAME)

            if (!userDataValidatorService.validateEmail(request.email))
                return@newSuspendedTransaction UserCreateResponse(status = UserCreateResponse.StatusType.BAD_MAIL)

            return@newSuspendedTransaction UserCreateResponse(userDTO =
            User.new {
                firstName = request.firstName
                secondName = request.secondName
                email = request.email
                username = request.username
                password = passwordService.hashPassword(request.password)
            }.toDTO()
            )
        }
    }

    override suspend fun updateUser(uuid: String, request: UserUpdateRequest): UserUpdateResponse {
        return newSuspendedTransaction {
            val user = User.findById(UUID.fromString(uuid))
                ?: return@newSuspendedTransaction UserUpdateResponse(status = UserUpdateResponse.StatusType.NOT_FOUND)

            if (
                request.username != null &&
                request.username != user.username &&
                !User.find { UserTable.username.lowerCase() eq request.username.lowercase() }.empty()
                )
                return@newSuspendedTransaction UserUpdateResponse(status = UserUpdateResponse.StatusType.USERNAME_ALREADY_IN_USE)

            if (
                request.email != null &&
                request.email != user.email &&
                !User.find { UserTable.email.lowerCase() eq request.email.lowercase() }.empty()
                )
                return@newSuspendedTransaction UserUpdateResponse(status = UserUpdateResponse.StatusType.EMAIL_ALREADY_IN_USE)

            if (!request.password.isNullOrEmpty() && !passwordService.validatePassword(request.password))
                return@newSuspendedTransaction UserUpdateResponse(status = UserUpdateResponse.StatusType.BAD_PASSWORD)

            if (!request.username.isNullOrEmpty() && !userDataValidatorService.validateUsername(request.username))
                return@newSuspendedTransaction UserUpdateResponse(status = UserUpdateResponse.StatusType.BAD_USERNAME)

            if (!request.firstName.isNullOrEmpty() && !userDataValidatorService.validateFirstOrSecondName(request.firstName))
                return@newSuspendedTransaction UserUpdateResponse(status = UserUpdateResponse.StatusType.BAD_NAME)

            if (!request.secondName.isNullOrEmpty() && !userDataValidatorService.validateFirstOrSecondName(request.secondName))
                return@newSuspendedTransaction UserUpdateResponse(status = UserUpdateResponse.StatusType.BAD_NAME)

            if (!request.email.isNullOrEmpty() && !userDataValidatorService.validateEmail(request.email))
                return@newSuspendedTransaction UserUpdateResponse(status = UserUpdateResponse.StatusType.BAD_MAIL)

            user.apply {
                if (!request.username.isNullOrEmpty()) username = request.username
                if (!request.firstName.isNullOrEmpty()) firstName = request.firstName
                if (!request.secondName.isNullOrEmpty()) secondName = request.secondName
                if (!request.email.isNullOrEmpty()) email = request.email
                if (!request.password.isNullOrEmpty()) password = passwordService.hashPassword(request.password)
            }

            if (!request.password.isNullOrEmpty())
                UserTokens.find { UserTokensTable.user eq user.id }.forEach { token -> token.delete() }

            return@newSuspendedTransaction UserUpdateResponse(
                userDTO = user.toDTO()
            )
        }
    }

    override suspend fun removeUser(uuid: String): UserDTO? {
        return newSuspendedTransaction {
            val _uuid = UUID.fromString(uuid)
            val dto = User.findById(_uuid)?.toDTO() ?: return@newSuspendedTransaction null

            User.findById(_uuid)?.delete()

            return@newSuspendedTransaction dto
        }
    }

    override suspend fun loginUser(request: UserLoginRequest): UserLoginResponse? {
        return newSuspendedTransaction {
            val user = User.find {
                    (UserTable.username.lowerCase() eq request.login.lowercase())
                        .or(UserTable.email.lowerCase() eq request.login.lowercase())
            }.firstOrNull()

            if (user == null || !passwordService.verifyPassword(request.password, user.password))
                return@newSuspendedTransaction null

            val token = tokenService.generateToken()

            UserTokens.new {
                this.token = token
                this.user = user
                this.validTime = LocalDateTime.now().plusDays(90)
            }

            return@newSuspendedTransaction UserLoginResponse(token, user.toDTO())
        }
    }

    override suspend fun logoutUser(token: String) {
        return newSuspendedTransaction {
            UserTokens.find { UserTokensTable.token eq token }.firstOrNull()?.delete()
        }
    }

    override suspend fun getTokenValidity(token: String): GetTokenValidityResponse {
        return newSuspendedTransaction {
            val userToken = UserTokens.find { UserTokensTable.token eq token }.firstOrNull()

            return@newSuspendedTransaction GetTokenValidityResponse(
                    userToken?.validTime?.toKotlinLocalDateTime() ?: LocalDateTime.MIN.toKotlinLocalDateTime(),
                    userToken?.user?.id?.toString()
            )
        }
    }
}