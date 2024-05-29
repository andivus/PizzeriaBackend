package me.dev.dependencyInjection

import me.dev.common.password.*
import me.dev.common.seed.SeedService
import me.dev.common.seed.SeedServiceImpl
import me.dev.common.token.TokenService
import me.dev.common.token.TokenServiceImpl
import me.dev.common.username.UserDataValidatorService
import me.dev.common.username.UserDataValidatorServiceImpl
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
import me.dev.feature.user.domain.UserRepository
import me.dev.feature.user.domain.UserRepositoryImpl

val appModule = module {
    singleOf(::KtorConfig)
    singleOf(::HikariDatabaseConnector) bind DatabaseConnector::class
    singleOf(::ItemRepositoryImpl) bind ItemRepository::class
    singleOf(::OrderRepositoryImpl) bind OrderRepository::class
    singleOf(::UserRepositoryImpl) bind UserRepository::class
    singleOf(::PasswordServiceImpl) bind PasswordService::class
    singleOf(::UserDataValidatorServiceImpl) bind UserDataValidatorService::class
    singleOf(::TokenServiceImpl) bind TokenService::class
    singleOf(::SeedServiceImpl) bind SeedService::class
}