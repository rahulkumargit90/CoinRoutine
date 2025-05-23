package com.rk.coinroutine

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rk.coinroutine.theme.CoinRoutineTheme
import com.rk.coinroutine.trade.presentation.buy.BuyScreen
import com.rk.coinroutine.trade.presentation.sell.SellScreen
import com.rk.coinroutine.core.navigation.Buy
import com.rk.coinroutine.core.navigation.Coins
import com.rk.coinroutine.core.navigation.Portfolio
import com.rk.coinroutine.core.navigation.Sell
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import com.rk.coinroutine.portfolio.presentation.PortfolioScreen
import com.rk.coinroutine.coin.presentaion.CoinsListScreen
import androidx.navigation.toRoute


@Composable
@Preview
fun App() {
    val navController: NavHostController = rememberNavController()
    CoinRoutineTheme {
        NavHost(
            navController = navController,
            startDestination = Portfolio,
            modifier = Modifier.fillMaxSize()
        ) {
            composable<Portfolio> {
                PortfolioScreen(
                    onCoinItemClicked = { coinId ->
                        navController.navigate(Sell(coinId))
                    },
                    onDiscoverCoinsClicked = {
                        navController.navigate(Coins)
                    }
                )
            }

            composable<Coins> {
                CoinsListScreen { coinId ->
                    navController.navigate(Buy(coinId))
                }
            }

            composable<Buy> { navBackStackEntry ->
                val coinId: String = navBackStackEntry.toRoute<Buy>().coinId
                BuyScreen(
                    coinId = coinId,
                    navigateToPortfolio = {
                        navController.navigate(Portfolio) {
                            popUpTo(Portfolio) { inclusive = true }
                        }
                    }
                )
            }
            composable<Sell> { navBackStackEntry ->
                val coinId: String = navBackStackEntry.toRoute<Sell>().coinId
                SellScreen(
                    coinId = coinId,
                    navigateToPortfolio = {
                        navController.navigate(Portfolio) {
                            popUpTo(Portfolio) { inclusive = true }
                        }
                    }
                )
            }

        }
    }
}