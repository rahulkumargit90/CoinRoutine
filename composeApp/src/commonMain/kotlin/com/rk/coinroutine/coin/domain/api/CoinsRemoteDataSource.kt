package com.rk.coinroutine.coin.domain.api

import com.rk.coinroutine.coin.data.remote.CoinDetailsResponseDto
import com.rk.coinroutine.coin.data.remote.CoinPriceHistoryResponseDto
import com.rk.coinroutine.coin.data.remote.CoinsResponseDto
import com.rk.coinroutine.core.domain.DataError
import com.rk.coinroutine.core.domain.Result

interface CoinsRemoteDataSource {

    suspend fun getListOfCoins(): Result<CoinsResponseDto, DataError.Remote>

    suspend fun getPriceHistory(coinId: String): Result<CoinPriceHistoryResponseDto, DataError.Remote>

    suspend fun getCoinById(coinId: String): Result<CoinDetailsResponseDto, DataError.Remote>

}