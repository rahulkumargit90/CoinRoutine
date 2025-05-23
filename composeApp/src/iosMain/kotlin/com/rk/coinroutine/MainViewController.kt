package com.rk.coinroutine

import androidx.compose.ui.window.ComposeUIViewController
import com.rk.coinroutine.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }