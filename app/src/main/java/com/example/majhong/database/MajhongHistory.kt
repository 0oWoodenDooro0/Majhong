package com.example.majhong.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MajhongHistory(
    val player1Id: Int,
    val score1: Int,
    val player2Id: Int,
    val score2: Int,
    val player3Id: Int,
    val score3: Int,
    val player4Id: Int,
    val score4: Int,
    val banker: Int,
    val continueToBank: Int,
    val round: Int,
    val wind: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)