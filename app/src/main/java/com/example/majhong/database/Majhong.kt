package com.example.majhong.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Majhong(
    val banker: Int,
    val continueToBank: Int,
    val round: Int,
    val wind: Int,
    val baseTai: Int,
    val tai: Int,
    val drawToContinue: Boolean,
    val newToClearPlayer: Boolean,
    val historySize: Int,
    @PrimaryKey
    val id: Int = 0
)