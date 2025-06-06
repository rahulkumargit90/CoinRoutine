package com.rk.coinroutine.portfolio.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("UserBalanceEntity")
data class UserBalanceEntity(
    @PrimaryKey val id: Int = 1,
    val cashBalance: Double
)