package com.rk.coinroutine.portfolio.domain

import com.rk.coinroutine.core.coin.Coin

data class PortfolioCoinModel(
    val coin: Coin,
    val performancePercent: Double,
    val averagePurchasePrice: Double,
    val ownedAmountInUnit: Double,
    val ownedAmountInFiat: Double,
)