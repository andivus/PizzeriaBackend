package me.dev.feature.user.domain

import me.dev.feature.user.domain.model.*

interface UserRepository {
    suspend fun getAllUsers(): List<UserDTO>
    suspend fun getUser(uuid: String): UserDTO?
    suspend fun createUser(request: UserCreateRequest): UserCreateResponse
    suspend fun updateUser(uuid: String, request: UserUpdateRequest): UserUpdateResponse
    suspend fun removeUser(uuid: String): UserDTO?
    suspend fun loginUser(request: UserLoginRequest): UserLoginResponse?
    suspend fun logoutUser(token: String)
    suspend fun getTokenValidity(token: String): GetTokenValidityResponse
}