package com.rk.coinroutine.trade.presentation.mapper

import com.rk.coinroutine.core.coin.Coin
import com.rk.coinroutine.trade.presentation.common.UiTradeCoinItem

fun UiTradeCoinItem.toCoin() = Coin(
    id = id,
    name = name,
    symbol = symbol,
    iconUrl = iconUrl,
)