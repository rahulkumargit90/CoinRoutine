package com.rk.coinroutine.trade.domain


import com.rk.coinroutine.core.coin.Coin
import com.rk.coinroutine.core.domain.DataError
import com.rk.coinroutine.core.domain.EmptyResult
import com.rk.coinroutine.core.domain.Result
import com.rk.coinroutine.portfolio.domain.PortfolioRepository
import kotlinx.coroutines.flow.first

class SellCoinUseCase(
    private val portfolioRepository: PortfolioRepository,
) {

    suspend fun sellCoin(
        coin: Coin,
        amountInFiat: Double,
        price: Double,
    ): EmptyResult<DataError> {
        val sellAllThreshold = 1
        when(val existingCoinResponse = portfolioRepository.getPortfolioCoin(coin.id)) {
            is Result.Success -> {
                val existingCoin = existingCoinResponse.data
                val sellAmountInUnit = amountInFiat / price

                val balance = portfolioRepository.cashBalanceFlow().first()
                if (existingCoin == null || existingCoin.ownedAmountInUnit < sellAmountInUnit) {
                    return Result.Error(DataError.Local.INSUFFICIENT_FUNDS)
                }
                val remainingAmountFiat = existingCoin.ownedAmountInFiat - amountInFiat
                val remainingAmountUnit = existingCoin.ownedAmountInUnit - sellAmountInUnit
                if (remainingAmountFiat < sellAllThreshold) {
                    portfolioRepository.removeCoinFromPortfolio(coin.id)
                } else {
                    portfolioRepository.savePortfolioCoin(
                        existingCoin.copy(
                            ownedAmountInUnit = remainingAmountUnit,
                            ownedAmountInFiat = remainingAmountFiat,
                        )
                    )
                }
                portfolioRepository.updateCashBalance(balance + amountInFiat)
                return Result.Success(Unit)
            }
            is Result.Error -> {
                return existingCoinResponse
            }
        }
    }


}