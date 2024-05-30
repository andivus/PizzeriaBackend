package me.dev.common.username

class UserDataValidatorServiceImpl : UserDataValidatorService {

    override fun validateUsername(name: String): Boolean {
        return name.matches(Regex("^[a-z0-9_-]{3,16}\$"))
    }

    override fun validateFirstOrSecondName(name: String): Boolean {
        return name.length in 1..64
    }

    override fun validateEmail(name: String): Boolean {
        return name.matches(Regex("[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+"))
    }
}