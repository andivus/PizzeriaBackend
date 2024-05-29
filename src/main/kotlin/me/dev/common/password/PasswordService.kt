package me.dev.common.password

interface PasswordService {
    fun validatePassword(password: String): Boolean
    fun hashPassword(password: String): String
    fun verifyPassword(password: String, hash: String): Boolean
    suspend fun verifyUserPassword(uuid: String, password: String): Boolean
}