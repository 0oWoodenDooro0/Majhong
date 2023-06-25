package com.example.majhong

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

fun Boolean.toInt() = if (this) 1 else 0

class PlayerViewModel : ViewModel() {
    private val _players = mutableStateListOf(Player("a"), Player("b"), Player("c"), Player("d"))
    val players: List<Player> = _players

    private val _baseTai = mutableStateOf(30)
    val baseTai: State<Int> = _baseTai

    private val _tai = mutableStateOf(10)
    val tai: State<Int> = _tai

    private val _banker = mutableStateOf(_players[0])
    val banker: State<Player> = _banker

    private val _continueToBank = mutableStateOf(0)
    val continueToBank: State<Int> = _continueToBank

    fun addPlayer(name: String) {
        val player = Player(name)
        _players.add(player)
    }

    fun updatePlayerName(player: Player, name: String) {
        player.name = name;
    }

    fun updatePlayerScore(player: Player, score: Int) {
        player.score += score;
    }

    fun getAllPlayerNamed(): Boolean {
        for (i in 0 until _players.size - 1) {
            if (players[i].name == "") return false
        }
        return true
    }

    fun getBanker(): Player {
        return _banker.value
    }

    fun calculateTotal(currentPlayer: Player, selectedPlayer: Player, numberOfTai: Int): Int {
        val currentIsBanker = currentPlayer == _banker.value || selectedPlayer == _banker.value
        val selfDraw = currentPlayer == selectedPlayer
        val selfDrawAndNotBanker = selfDraw && !currentIsBanker
        return (_baseTai.value + _tai.value * numberOfTai + currentIsBanker.toInt() * _tai.value * (2 * _continueToBank.value + 1)) * (1 + selfDraw.toInt() * 2) + selfDrawAndNotBanker.toInt() * _tai.value * (2 * _continueToBank.value + 1)
    }

    fun updateScore(currentPlayer: Player, chuckPlayer: Player) {

    }
}

data class Player(var name: String = "", var score: Int = 0)