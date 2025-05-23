package com.rk.coinroutine.coin.domain.model

import com.rk.coinroutine.core.coin.Coin

data class CoinModel(
    val coin: Coin,
    val price: Double,
    val change: Double,
)