package com.rk.coinroutine.coin.data.mapper

import com.rk.coinroutine.coin.data.remote.CoinItemDto
import com.rk.coinroutine.coin.data.remote.CoinPriceDto
import com.rk.coinroutine.coin.domain.model.CoinModel
import com.rk.coinroutine.coin.domain.model.PriceModel
import com.rk.coinroutine.core.coin.Coin


fun CoinItemDto.toCoinModel() = CoinModel(
    coin = Coin(
        id = uuid,
        name = name,
        symbol = symbol,
        iconUrl = iconUrl,
    ),
    price = price,
    change = change,
)

fun CoinPriceDto.toPriceModel() = PriceModel(
    price = price ?: 0.0,
    timestamp = timestamp,
)