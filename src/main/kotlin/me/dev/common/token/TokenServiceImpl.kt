package me.dev.common.token

import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence

class TokenServiceImpl : TokenService {
    override fun generateToken(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return ThreadLocalRandom.current()
            .ints(72, 0, charPool.size)
            .asSequence()
            .map(charPool::get)
            .joinToString("")
    }
}