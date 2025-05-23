package com.rk.coinroutine.presentation

import app.cash.turbine.test
import com.rk.coinroutine.core.domain.DataError
import com.rk.coinroutine.core.util.formatFiat
import com.rk.coinroutine.core.util.toUiText
import com.rk.coinroutine.portfolio.data.FakePortfolioRepository
import com.rk.coinroutine.portfolio.presentation.PortfolioViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PortfolioViewModelTest {
    private lateinit var viewModel: PortfolioViewModel
    private lateinit var portfolioRepository: FakePortfolioRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        portfolioRepository = FakePortfolioRepository()
        viewModel = PortfolioViewModel(
            portfolioRepository = portfolioRepository,
            coroutineDispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `State and portfolio coins are properly combined`() = runTest {
        viewModel.state.test {
            val initialState = awaitItem()
            assertTrue(initialState.coins.isEmpty())

            val portfolioCoin = FakePortfolioRepository.portfolioCoin
            portfolioRepository.savePortfolioCoin(portfolioCoin)

            awaitItem() // Ignore the first emission
            val updatedState = awaitItem()
            assertTrue(updatedState.coins.isNotEmpty())
            assertFalse(updatedState.isLoading)
            assertEquals(FakePortfolioRepository.portfolioCoin.coin.id, updatedState.coins.first().id)
        }
    }

    @Test
    fun `Portfolio value updates when a coin is added`() = runTest {
        viewModel.state.test {
            val initialState = awaitItem()
            assertEquals(initialState.portfolioValue, formatFiat(10000.0))

            val portfolioCoin = FakePortfolioRepository.portfolioCoin.copy(
                ownedAmountInUnit = 50.0,
                ownedAmountInFiat = 1000.0
            )
            portfolioRepository.savePortfolioCoin(portfolioCoin)
            val updatedState = awaitItem()
            assertEquals(formatFiat(11000.0), updatedState.portfolioValue)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Loading state and error message update on failure`() = runTest {
        portfolioRepository.simulateError()

        viewModel.state.test {
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertEquals(DataError.Remote.SERVER.toUiText(), errorState.error)
        }
    }


}