package me.dev.feature.user.domain

import me.dev.feature.user.domain.model.*

interface UserRepository {
    suspend fun getAllUsers(): List<UserDTO>
    suspend fun getUser(uuid: String): UserDTO?
    suspend fun createUser(request: UserCreateRequest): UserCreateResponse
    suspend fun updateUserInfo(uuid: String, request: UserUpdateInfoRequest): UserDTO?
    suspend fun updateUserCred(uuid: String, request: UserUpdateCredRequest): UserDTO?
    suspend fun removeUser(uuid: String): UserDTO?
    suspend fun loginUser(request: UserLoginRequest): UserLoginResponse?
    suspend fun logoutUser(token: String)
    suspend fun getTokenValidity(token: String): GetTokenValidityResponse
}