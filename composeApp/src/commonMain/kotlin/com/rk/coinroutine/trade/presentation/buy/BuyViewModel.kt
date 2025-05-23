package com.rk.coinroutine.trade.presentation.buy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.coinroutine.coin.domain.usecase.GetCoinDetailsUseCase
import com.rk.coinroutine.portfolio.domain.PortfolioRepository
import com.rk.coinroutine.trade.domain.BuyCoinUseCase
import com.rk.coinroutine.trade.presentation.common.TradeState
import com.rk.coinroutine.trade.presentation.mapper.toCoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.rk.coinroutine.core.domain.Result
import com.rk.coinroutine.core.util.formatFiat
import com.rk.coinroutine.core.util.toUiText
import com.rk.coinroutine.trade.presentation.common.UiTradeCoinItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow


class BuyViewModel(
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
    private val portfolioRepository: PortfolioRepository,
    private val buyCoinUseCase: BuyCoinUseCase,
    private val coinId: String,
) : ViewModel() {

    private val _amount = MutableStateFlow("")
    private val _state = MutableStateFlow(TradeState())
    val state = combine(
        _state,
        _amount,
    ) { state, amount ->
        state.copy(
            amount = amount
        )
    }.onStart {
        val balance = portfolioRepository.cashBalanceFlow().first()
        getCoinDetails(balance)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TradeState(isLoading = true)
    )

    private val _events = Channel<BuyEvents>(capacity = Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private suspend fun getCoinDetails(balance: Double) {
        when (val coinResponse = getCoinDetailsUseCase.execute(coinId)) {
            is Result.Success -> {
                _state.update {
                    it.copy(
                        coin = UiTradeCoinItem(
                            id = coinResponse.data.coin.id,
                            name = coinResponse.data.coin.name,
                            symbol = coinResponse.data.coin.symbol,
                            iconUrl = coinResponse.data.coin.iconUrl,
                            price = coinResponse.data.price,
                        ),
                        availableAmount = "Available: ${formatFiat(balance)}"
                    )
                }
            }

            is Result.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = coinResponse.error.toUiText()
                    )
                }
            }
        }
    }

    fun onAmountChanged(amount: String) {
        _amount.value = amount
    }

    fun onBuyClicked() {
        val tradeCoin = state.value.coin ?: return
        viewModelScope.launch {
            val buyCoinResponse = buyCoinUseCase.buyCoin(
                coin = tradeCoin.toCoin(),
                amountInFiat = _amount.value.toDouble(),
                price = tradeCoin.price,
            )

            when(buyCoinResponse) {
                is Result.Success -> {
                    _events.send(BuyEvents.BuySuccess)
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = buyCoinResponse.error.toUiText(),
                        )
                    }
                }
            }
        }
    }
}

sealed interface BuyEvents {
    data object BuySuccess : BuyEvents
}