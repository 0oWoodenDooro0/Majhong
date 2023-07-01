package com.example.majhong.database

import com.example.majhong.PlayerState

sealed interface MajhongDatabaseEvent {
    object InitMajhongAndPlayerDatabase : MajhongDatabaseEvent
    data class UpsertPlayer(val player: Player) : MajhongDatabaseEvent
    data class UpdatePlayerScore(val score: Int, val direction: Int) : MajhongDatabaseEvent
    data class UpsertMajhongDatabase(val majhong: Majhong) : MajhongDatabaseEvent
    data class UpsertNewMajhongDatabase(
        val baseTai: Int,
        val tai: Int,
        val drawToContinue: Boolean,
        val newToClearPlayer: Boolean
    ) : MajhongDatabaseEvent

    object GetAllPlayer : MajhongDatabaseEvent
    data class SwapPlayer(val player1: PlayerState, val player2: PlayerState) : MajhongDatabaseEvent
}