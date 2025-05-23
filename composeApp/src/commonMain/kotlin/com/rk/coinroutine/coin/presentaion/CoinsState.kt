package com.rk.coinroutine.coin.presentaion

import androidx.compose.runtime.Stable
import org.jetbrains.compose.resources.StringResource


@Stable
data class CoinsState(
    val error: StringResource? = null,
    val coins: List<UiCoinListItem> = emptyList(),
    val chartState: UiChartState? = null
)

data class UiChartState(
    val sparkLine: List<Double> = emptyList(),
    val isLoading: Boolean = false,
    val coinName: String = "",
)