package com.rk.coinroutine.coin.domain.usecase

import com.rk.coinroutine.coin.data.mapper.toCoinModel
import com.rk.coinroutine.coin.domain.api.CoinsRemoteDataSource
import com.rk.coinroutine.coin.domain.model.CoinModel
import com.rk.coinroutine.core.domain.DataError
import com.rk.coinroutine.core.domain.Result
import com.rk.coinroutine.core.domain.map


class GetCoinsListUseCase(
    private val client: CoinsRemoteDataSource,
) {
    suspend fun execute(): Result<List<CoinModel>, DataError.Remote> {
        return client.getListOfCoins().map { dto ->
            dto.data.coins.map { it.toCoinModel() }
        }
    }

}

