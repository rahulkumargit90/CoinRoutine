package com.rk.coinroutine.coin.data.impl

import com.rk.coinroutine.coin.data.remote.CoinDetailsResponseDto
import com.rk.coinroutine.coin.data.remote.CoinPriceHistoryResponseDto
import com.rk.coinroutine.coin.data.remote.CoinsResponseDto
import com.rk.coinroutine.coin.domain.api.CoinsRemoteDataSource
import com.rk.coinroutine.core.domain.DataError
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import com.rk.coinroutine.core.network.safeCall
import com.rk.coinroutine.core.domain.Result


private const val BASE_URL = "https://api.coinranking.com/v2"

class KtorCoinsRemoteDataSource(
    private val httpClient: HttpClient
) : CoinsRemoteDataSource {

    override suspend fun getListOfCoins(): Result<CoinsResponseDto, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/coins")
        }
    }

    override suspend fun getPriceHistory(coinId: String): Result<CoinPriceHistoryResponseDto, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/coin/$coinId/history")
        }
    }

    override suspend fun getCoinById(coinId: String): Result<CoinDetailsResponseDto, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/coin/$coinId")
        }
    }
}