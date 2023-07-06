package com.example.majhong.database

sealed interface MajhongDatabaseEvent {
    object InitMajhongAndPlayerDatabase : MajhongDatabaseEvent
    data class UpsertPlayer(val player: Player) : MajhongDatabaseEvent
    data class UpdatePlayerScore(val score: Int, val direction: Int) : MajhongDatabaseEvent
    object UpsertMajhongDatabase : MajhongDatabaseEvent
    data class UpsertNewMajhongDatabase(
        val baseTai: Int,
        val tai: Int,
        val drawToContinue: Boolean,
        val newToClearPlayer: Boolean
    ) : MajhongDatabaseEvent

    object GetAllPlayer : MajhongDatabaseEvent
    data class SwapPlayer(val player1: Player, val player2: Player) : MajhongDatabaseEvent
    data class UpsertMajhongHistory(val players: List<Player>) : MajhongDatabaseEvent
    object DeleteAllMajhongHistory : MajhongDatabaseEvent
    object Undo : MajhongDatabaseEvent
    data class UpdatePlayerChunkCountById(val id: Int, val count: Int) : MajhongDatabaseEvent
    data class UpdatePlayerSelfDrawnCountById(val id: Int, val count: Int) : MajhongDatabaseEvent
    data class UpdatePlayerWinCountById(val id: Int, val count: Int) : MajhongDatabaseEvent
}