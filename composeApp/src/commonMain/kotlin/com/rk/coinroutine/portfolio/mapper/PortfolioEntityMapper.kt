package com.rk.coinroutine.portfolio.mapper

import com.rk.coinroutine.core.coin.Coin
import com.rk.coinroutine.portfolio.data.local.PortfolioCoinEntity
import com.rk.coinroutine.portfolio.domain.PortfolioCoinModel
import kotlinx.datetime.Clock

fun PortfolioCoinEntity.toPortfolioCoinModel(
    currentPrice: Double,
): PortfolioCoinModel {
    return PortfolioCoinModel(
        coin = Coin(
            id = coinId,
            name = name,
            symbol = symbol,
            iconUrl = iconUrl
        ),
        performancePercent = ((currentPrice - averagePurchasePrice) / averagePurchasePrice) * 100,
        averagePurchasePrice = averagePurchasePrice,
        ownedAmountInUnit = amountOwned,
        ownedAmountInFiat = amountOwned * currentPrice,
    )
}

fun PortfolioCoinModel.toPortfolioCoinEntity(): PortfolioCoinEntity {
    return PortfolioCoinEntity(
        coinId = coin.id,
        name = coin.name,
        symbol = coin.symbol,
        iconUrl = coin.iconUrl,
        amountOwned = ownedAmountInUnit,
        averagePurchasePrice = averagePurchasePrice,
        timestamp = Clock.System.now().toEpochMilliseconds()
    )
}
