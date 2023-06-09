package ua.vn.div.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exposedLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject
import ua.vn.div.configuration.element.KtorConfig
import ua.vn.div.database.DatabaseConnector
import ua.vn.div.feature.item.data.ItemTable

fun Application.configureDatabase(){
    val ktorConfig by inject<KtorConfig>()
    val databaseProvider by inject<DatabaseConnector>()
    databaseProvider.connect()

    if (ktorConfig.development) {
        transaction {
            val changes = SchemaUtils.statementsRequiredToActualizeScheme(ItemTable).joinToString("\n")
            exposedLogger.info(if (changes.isNotBlank()) "Database schema needs to be updated with the following changes:\n$changes" else "Database schema is up to date")
        }
    }
}