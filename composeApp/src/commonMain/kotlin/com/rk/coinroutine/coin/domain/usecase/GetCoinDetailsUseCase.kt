package com.rk.coinroutine.coin.domain.usecase

import com.rk.coinroutine.coin.data.mapper.toCoinModel
import com.rk.coinroutine.coin.domain.api.CoinsRemoteDataSource
import com.rk.coinroutine.coin.domain.model.CoinModel
import com.rk.coinroutine.core.domain.DataError
import com.rk.coinroutine.core.domain.Result
import com.rk.coinroutine.core.domain.map


class GetCoinDetailsUseCase(
    private val client: CoinsRemoteDataSource,
) {
    suspend fun execute(coinId: String): Result<CoinModel, DataError.Remote> {
        return client.getCoinById(coinId).map { dto ->
            dto.data.coin.toCoinModel()
        }
    }
}