package com.example.majhong.database

sealed interface MajhongEvent {
    data class UpsertPlayer(val player: Player) : MajhongEvent
    data class UpdatePlayerScore(val score: Int, val direction: Int) : MajhongEvent
    data class UpsertMajhong(val majhong: Majhong) : MajhongEvent
}