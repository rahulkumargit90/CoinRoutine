package com.rk.coinroutine.trade.presentation.buy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.coinroutine.trade.presentation.common.TradeScreen
import com.rk.coinroutine.trade.presentation.common.TradeType
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle


@Composable
fun BuyScreen(
    coinId: String,
    navigateToPortfolio: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel = koinViewModel<BuyViewModel>(
        parameters = {
            parametersOf(coinId)
        }
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is BuyEvents.BuySuccess -> {
                        navigateToPortfolio()
                    }
                }
            }
        }
    }

    TradeScreen(
        state = state,
        tradeType = TradeType.BUY,
        onAmountChange = viewModel::onAmountChanged,
        onSubmitClicked = viewModel::onBuyClicked
    )
}