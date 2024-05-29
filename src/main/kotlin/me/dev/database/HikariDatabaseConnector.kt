package me.dev.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

import me.dev.configuration.element.KtorConfig

class HikariDatabaseConnector(private val config: KtorConfig) : DatabaseConnector {
    override fun connect() {
        val hikariConfig = HikariConfig()
        hikariConfig.driverClassName = config.databaseConfig.driverClass
        hikariConfig.jdbcUrl = config.databaseConfig.url
        hikariConfig.username = config.databaseConfig.user
        hikariConfig.password = config.databaseConfig.password
        hikariConfig.maximumPoolSize = config.databaseConfig.maxPoolSize
        hikariConfig.isAutoCommit = false
        hikariConfig.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        hikariConfig.validate()

        val hikariDataSource = HikariDataSource(hikariConfig)

        Database.connect(HikariDataSource(hikariConfig))

        Flyway.configure()
            .baselineOnMigrate(true)
            .dataSource(hikariDataSource)
            .load()
            .migrate()
    }

    override fun close() {
    }
}