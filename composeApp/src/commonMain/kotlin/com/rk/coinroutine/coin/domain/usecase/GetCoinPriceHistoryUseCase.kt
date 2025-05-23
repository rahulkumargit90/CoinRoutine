package com.rk.coinroutine.coin.domain.usecase

import com.rk.coinroutine.coin.data.mapper.toPriceModel
import com.rk.coinroutine.coin.domain.api.CoinsRemoteDataSource
import com.rk.coinroutine.coin.domain.model.PriceModel
import com.rk.coinroutine.core.domain.DataError
import com.rk.coinroutine.core.domain.Result
import com.rk.coinroutine.core.domain.map


class GetCoinPriceHistoryUseCase(
    private val client: CoinsRemoteDataSource,
) {
    suspend fun execute(coinId: String): Result<List<PriceModel>, DataError.Remote> {
        return client.getPriceHistory(coinId).map { dto ->
            dto.data.history.map { it.toPriceModel() }
        }
    }
}