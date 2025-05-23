@file:JvmName("AndroidSpecificModule") // Add this line
package com.rk.coinroutine.di

import androidx.room.RoomDatabase
import com.rk.coinroutine.core.database.getPortfolioDatabaseBuilder
import com.rk.coinroutine.core.database.portfolio.PortfolioDatabase
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {
    // core
    single<HttpClientEngine> { Android.create() }
    singleOf(::getPortfolioDatabaseBuilder).bind<RoomDatabase.Builder<PortfolioDatabase>>()


}