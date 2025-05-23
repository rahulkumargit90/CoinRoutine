package com.rk.coinroutine.di

import androidx.room.RoomDatabase
import com.rk.coinroutine.coin.data.impl.KtorCoinsRemoteDataSource
import com.rk.coinroutine.coin.domain.usecase.GetCoinDetailsUseCase
import com.rk.coinroutine.coin.domain.api.CoinsRemoteDataSource
import com.rk.coinroutine.coin.domain.usecase.GetCoinPriceHistoryUseCase
import com.rk.coinroutine.coin.domain.usecase.GetCoinsListUseCase
import com.rk.coinroutine.coin.presentaion.CoinsListViewModel
import com.rk.coinroutine.core.database.portfolio.PortfolioDatabase
import com.rk.coinroutine.core.database.portfolio.getPortfolioDatabase
import com.rk.coinroutine.core.network.HttpClientFactory
import com.rk.coinroutine.portfolio.data.PortfolioRepositoryImpl
import com.rk.coinroutine.portfolio.domain.PortfolioRepository
import com.rk.coinroutine.portfolio.presentation.PortfolioViewModel
import com.rk.coinroutine.trade.domain.BuyCoinUseCase
import com.rk.coinroutine.trade.domain.SellCoinUseCase
import com.rk.coinroutine.trade.presentation.buy.BuyViewModel
import com.rk.coinroutine.trade.presentation.sell.SellViewModel
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)
        modules(
            sharedModule,
            platformModule,
        )
    }

expect val platformModule: Module

val sharedModule = module {
    // core
    single<HttpClient> { HttpClientFactory.create(get()) }

    // portfolio
    single {
        getPortfolioDatabase(get<RoomDatabase.Builder<PortfolioDatabase>>())
    }

    singleOf(::PortfolioRepositoryImpl).bind<PortfolioRepository>()
    single { get<PortfolioDatabase>().portfolioDao() }
    single { get<PortfolioDatabase>().userBalanceDao() }
    viewModel { PortfolioViewModel(get()) }

    // coins list
    viewModel { CoinsListViewModel(get(), get()) }
    singleOf(::GetCoinsListUseCase)
    singleOf(::KtorCoinsRemoteDataSource).bind<CoinsRemoteDataSource>()
    singleOf(::GetCoinDetailsUseCase)
    singleOf(::GetCoinPriceHistoryUseCase)

    // trade
    singleOf(::BuyCoinUseCase)
    singleOf(::SellCoinUseCase)
    viewModel { (coinId: String) -> BuyViewModel(get(), get(), get(), coinId) }
    viewModel { (coinId: String) -> SellViewModel(get(), get(), get(), coinId) }


}