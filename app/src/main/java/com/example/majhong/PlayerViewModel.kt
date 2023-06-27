package com.example.majhong

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

fun Boolean.toInt() = if (this) 1 else 0

class PlayerViewModel : ViewModel() {

    private val _players =
        mutableStateListOf(Player("a"), Player("b"), Player("c"), Player("d"))
    val players: List<Player> = _players

    private val _directions = listOf("東", "南", "西", "北")

    var baseTai = mutableStateOf(30)
        private set

    var tai = mutableStateOf(10)
        private set

    var banker = mutableStateOf(_players[0])
        private set

    var continueToBank = mutableStateOf(0)
        private set

    var round = mutableStateOf(_directions[0])
        private set

    var wind = mutableStateOf(_directions[0])
        private set

    fun updatePlayerName(player: Player, name: String) {
        val index = _players.indexOf(player)
        _players[index].name = name
    }

    private fun updatePlayerScore(player: Player, score: Int) {
        val index = _players.indexOf(player)
        _players[index].score.value += score
    }

    fun isAllPlayerNamed(): Boolean {
        for (i in 0 until _players.size - 1) {
            if (players[i].name == "") return false
        }
        return true
    }

    fun draw() {
        updateContinueToBank()
    }

    private fun updateNextBanker() {
        val index = players.indexOf(banker.value)
        banker.value = players[(index + 1) % 4]
        continueToBank.value = 0
        updateWind()
    }

    private fun updateContinueToBank() {
        continueToBank.value += 1
    }

    private fun updateWind() {
        val index = _directions.indexOf(wind.value)
        wind.value = _directions[(index + 1) % 4]
        if (wind.value == "東") {
            updateRound()
        }
    }

    private fun updateRound() {
        val index = _directions.indexOf(round.value)
        round.value = _directions[(index + 1) % 4]
    }

    fun calculateTotal(currentPlayer: Player, selectedPlayer: Player, numberOfTai: Int): Int {
        val currentIsBanker = currentPlayer == banker.value || selectedPlayer == banker.value
        val selfDraw = currentPlayer == selectedPlayer
        val selfDrawAndNotBanker = selfDraw && !currentIsBanker
        return (baseTai.value + tai.value * numberOfTai + currentIsBanker.toInt() * tai.value * (2 * continueToBank.value + 1)) * (1 + selfDraw.toInt() * 2) + selfDrawAndNotBanker.toInt() * tai.value * (2 * continueToBank.value + 1)
    }

    fun updateScore(currentPlayer: Player, selectedPlayer: Player, numberOfTai: Int) {
        val selectedIsBanker = selectedPlayer == banker.value
        val currentIsBanker = currentPlayer == banker.value
        val isBanker = currentIsBanker || selectedIsBanker
        val selfDraw = currentPlayer == selectedPlayer
        val selfDrawAndNotBanker = selfDraw && !isBanker
        val currentTotal =
            (baseTai.value + tai.value * numberOfTai + isBanker.toInt() * tai.value * (2 * continueToBank.value + 1)) * (1 + selfDraw.toInt() * 2) + selfDrawAndNotBanker.toInt() * tai.value * (2 * continueToBank.value + 1)
        if (selfDrawAndNotBanker) {
            for (i in players) {
                if (currentPlayer != i) {
                    if (i != banker.value) {
                        updatePlayerScore(i, -(baseTai.value + tai.value * numberOfTai))
                    } else {
                        updatePlayerScore(
                            i,
                            -(baseTai.value + tai.value * numberOfTai + tai.value * (2 * continueToBank.value + 1))
                        )
                    }
                }
            }
        } else if (selfDraw) {
            for (i in players) {
                if (currentPlayer != i) {
                    updatePlayerScore(
                        i,
                        -(baseTai.value + tai.value * numberOfTai + tai.value * (2 * continueToBank.value + 1))
                    )
                }
            }
        } else if (isBanker) {
            updatePlayerScore(
                selectedPlayer,
                -(baseTai.value + tai.value * numberOfTai + tai.value * (2 * continueToBank.value + 1))
            )
        } else {
            updatePlayerScore(selectedPlayer, -(baseTai.value + tai.value * numberOfTai))
        }
        updatePlayerScore(currentPlayer, currentTotal)
        if (currentIsBanker) {
            updateContinueToBank()
        } else {
            updateNextBanker()
        }
    }
}

data class Player(var name: String = "", var score: MutableState<Int> = mutableStateOf(0))