package me.dev.dependencyInjection

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import me.dev.configuration.element.KtorConfig
import me.dev.database.DatabaseConnector
import me.dev.database.HikariDatabaseConnector
import me.dev.feature.item.domain.ItemRepository
import me.dev.feature.item.domain.ItemRepositoryImpl
import me.dev.feature.order.domain.OrderRepository
import me.dev.feature.order.domain.OrderRepositoryImpl

val appModule = module {
    singleOf(::KtorConfig)
    singleOf(::HikariDatabaseConnector) bind DatabaseConnector::class
    singleOf(::ItemRepositoryImpl) bind ItemRepository::class
    singleOf(::OrderRepositoryImpl) bind OrderRepository::class

}