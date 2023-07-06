package com.example.majhong.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MajhongHistory(
    val player1Id: Int = 0,
    val score1: Int = 0,
    val player2Id: Int = 0,
    val score2: Int = 0,
    val player3Id: Int = 0,
    val score3: Int = 0,
    val player4Id: Int = 0,
    val score4: Int = 0,
    val banker: Int = 0,
    val continueToBank: Int = 0,
    val round: Int = 0,
    val wind: Int = 0,
    @PrimaryKey
    val id: Int = 0
)