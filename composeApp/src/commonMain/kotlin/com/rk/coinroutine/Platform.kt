package com.rk.coinroutine

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform