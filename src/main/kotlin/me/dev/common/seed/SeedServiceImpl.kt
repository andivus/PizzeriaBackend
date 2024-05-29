package me.dev.common.seed

import me.dev.common.password.PasswordService
import me.dev.feature.user.data.User
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SeedServiceImpl : SeedService, KoinComponent {
    private val passwordService: PasswordService by inject()
    override suspend fun seed() {
        newSuspendedTransaction {
            if (!User.all().empty()) return@newSuspendedTransaction

            User.new {
                firstName = "Admin"
                secondName = "Root"
                email = "admin@pizzeria.com"
                username = "admin"
                password = passwordService.hashPassword("admin")
            }

        }
    }
}