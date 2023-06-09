package ua.vn.div.dependencyInjection

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ua.vn.div.configuration.element.KtorConfig
import ua.vn.div.database.DatabaseConnector
import ua.vn.div.database.HikariDatabaseConnector
import ua.vn.div.feature.item.domain.ItemRepository
import ua.vn.div.feature.item.domain.ItemRepositoryImpl

val appModule = module {
    singleOf(::KtorConfig)
    singleOf(::HikariDatabaseConnector) bind DatabaseConnector::class
    singleOf(::ItemRepositoryImpl) bind ItemRepository::class

}