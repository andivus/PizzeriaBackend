package me.dev.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exposedLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject
import me.dev.configuration.element.KtorConfig
import me.dev.database.DatabaseConnector
import me.dev.feature.item.data.ItemTable
import me.dev.feature.order.data.OrderItemTable
import me.dev.feature.order.data.OrderTable

fun Application.configureDatabase(){
    val ktorConfig by inject<KtorConfig>()
    val databaseProvider by inject<DatabaseConnector>()
    databaseProvider.connect()

    if (ktorConfig.development) {
        transaction {
            val changes = SchemaUtils.statementsRequiredToActualizeScheme(ItemTable, OrderTable, OrderItemTable).joinToString("\n")
            exposedLogger.info(if (changes.isNotBlank()) "Database schema needs to be updated with the following changes:\n$changes" else "Database schema is up to date")
        }
    }
}