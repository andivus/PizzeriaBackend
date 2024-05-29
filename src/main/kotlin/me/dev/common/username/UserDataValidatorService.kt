package me.dev.common.username

interface UserDataValidatorService {
    fun validateUsername(name: String): Boolean
    fun validateFirstOrSecondName(name: String): Boolean
    fun validateEmail(name: String): Boolean
}