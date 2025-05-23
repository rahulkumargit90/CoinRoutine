package com.rk.coinroutine.di

import androidx.room.RoomDatabase
import com.rk.coinroutine.core.database.getPortfolioDatabaseBuilder
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import com.rk.coinroutine.core.database.portfolio.PortfolioDatabase

actual val platformModule = module {
    single<HttpClientEngine> { Darwin.create() }
    singleOf(::getPortfolioDatabaseBuilder).bind<RoomDatabase.Builder<PortfolioDatabase>>()


}