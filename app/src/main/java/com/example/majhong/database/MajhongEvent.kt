package com.example.majhong.database

sealed interface MajhongEvent {
    data class ModifyRules(
        val baseTai: Int,
        val tai: Int,
        val drawToContinue: Boolean,
        val newToClearPlayer: Boolean
    ) : MajhongEvent

    data class SwapPlayer(val player1: Player, val player2: Player) : MajhongEvent
    object Undo : MajhongEvent
    data class AddNewPlayer(val player: Player, val name: String, val direction: Int = -1) :
        MajhongEvent

    data class UpdateScore(
        val currentPlayer: Player,
        val selectedPlayer: Player,
        val numberOfTai: Int
    ) : MajhongEvent

    data class ResetBanker(
        val bankerIndex: Int,
        val resetContinue: Boolean,
        val resetRoundWind: Boolean
    ) : MajhongEvent

    object Draw : MajhongEvent
}