package com.example.majhong.database

sealed interface MajhongEvent {
    object InitMajhongAndPlayer : MajhongEvent
    data class UpsertPlayer(val player: Player) : MajhongEvent
    data class UpdatePlayerScore(val score: Int, val direction: Int) : MajhongEvent
    data class UpsertMajhong(val majhong: Majhong) : MajhongEvent
    data class CreateNewMajhong(val baseTai: Int, val tai: Int, val drawToContinue: Boolean) : MajhongEvent
}