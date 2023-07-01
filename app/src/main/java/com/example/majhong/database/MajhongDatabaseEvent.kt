package com.example.majhong.database

sealed interface MajhongDatabaseEvent {
    object InitMajhongAndPlayerDatabase : MajhongDatabaseEvent
    data class UpsertPlayer(val player: Player) : MajhongDatabaseEvent
    data class UpdatePlayerScore(val score: Int, val direction: Int) : MajhongDatabaseEvent
    data class UpsertMajhongDatabase(val majhong: Majhong) : MajhongDatabaseEvent
    data class UpsertNewMajhongDatabase(val baseTai: Int, val tai: Int, val drawToContinue: Boolean) : MajhongDatabaseEvent
}