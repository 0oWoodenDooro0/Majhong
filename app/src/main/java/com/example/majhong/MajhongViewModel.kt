package com.example.majhong

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.majhong.database.Majhong
import com.example.majhong.database.MajhongDao
import com.example.majhong.database.MajhongDatabaseEvent
import com.example.majhong.database.Player
import com.example.majhong.database.PlayerDao
import kotlinx.coroutines.launch

fun Boolean.toInt() = if (this) 1 else 0

class MajhongViewModel(
    private val playerDao: PlayerDao, private val majhongDao: MajhongDao
) : ViewModel() {

    var players by mutableStateOf(listOf(Player(), Player(), Player(), Player()))

    val directions = listOf("東", "南", "西", "北")

    private var banker by mutableStateOf(0)

    var continueToBank by mutableStateOf(0)
        private set

    var round by mutableStateOf(0)
        private set

    var wind by mutableStateOf(0)
        private set

    var baseTai by mutableStateOf(30)
        private set

    var tai by mutableStateOf(10)
        private set

    var drawToContinue by mutableStateOf(true)
        private set

    var newToClearPlayer by mutableStateOf(false)
        private set

    fun onDatabaseEvent(event: MajhongDatabaseEvent) {
        when (event) {
            is MajhongDatabaseEvent.InitMajhongAndPlayerDatabase -> {
                viewModelScope.launch {
                    players = playerDao.getAllPlayer()
                    val majhong = majhongDao.getMajhongById()
                    if (majhong != null) {
                        banker = majhong.banker
                        continueToBank = majhong.continueToBank
                        round = majhong.round
                        wind = majhong.wind
                        baseTai = majhong.baseTai
                        tai = majhong.tai
                        drawToContinue = majhong.drawToContinue
                        newToClearPlayer = majhong.newToClearPlayer
                    } else {
                        majhongDao.upsertMajhong(
                            Majhong(
                                banker = 0,
                                continueToBank = 0,
                                round = 0,
                                wind = 0,
                                baseTai = 30,
                                tai = 10,
                                drawToContinue = true,
                                newToClearPlayer = false
                            )
                        )
                    }
                }
            }

            is MajhongDatabaseEvent.UpsertPlayer -> {
                viewModelScope.launch {
                    viewModelScope.launch {
                        playerDao.upsertPlayer(event.player)
                    }
                }
            }

            is MajhongDatabaseEvent.UpdatePlayerScore -> {
                viewModelScope.launch {
                    playerDao.updatePlayerScoreByDirection(event.score, event.direction)
                }
            }

            is MajhongDatabaseEvent.UpsertMajhongDatabase -> {
                viewModelScope.launch {
                    majhongDao.upsertMajhong(
                        Majhong(
                            banker,
                            continueToBank,
                            round,
                            wind,
                            baseTai,
                            tai,
                            drawToContinue,
                            newToClearPlayer
                        )
                    )
                }
            }

            is MajhongDatabaseEvent.UpsertNewMajhongDatabase -> {
                viewModelScope.launch {
                    majhongDao.upsertMajhong(
                        Majhong(
                            0,
                            0,
                            0,
                            0,
                            event.baseTai,
                            event.tai,
                            event.drawToContinue,
                            event.newToClearPlayer
                        )
                    )
                    if (event.newToClearPlayer) playerDao.deleteAllPlayer()
                    else playerDao.setAllPlayerScoreToZero()
                    onDatabaseEvent(MajhongDatabaseEvent.InitMajhongAndPlayerDatabase)
                }
            }

            is MajhongDatabaseEvent.GetAllPlayer -> {
                viewModelScope.launch {
                    players = playerDao.getAllPlayer()
                }
            }

            is MajhongDatabaseEvent.SwapPlayer -> {
                viewModelScope.launch {
                    playerDao.updatePlayerDirectionById(
                        event.player2.direction, event.player1.id
                    )
                    playerDao.updatePlayerDirectionById(
                        event.player1.direction, event.player2.id
                    )
                    onDatabaseEvent(MajhongDatabaseEvent.GetAllPlayer)
                    onDatabaseEvent(MajhongDatabaseEvent.InitMajhongAndPlayerDatabase)
                }
            }
        }
    }

    init {
        onDatabaseEvent(MajhongDatabaseEvent.InitMajhongAndPlayerDatabase)
    }

    fun getPlayerByDirection(direction: Int): Player {
        for (i in players) {
            if (i.direction == direction) return i
        }
        return Player()
    }

    private fun getBanker(): Player {
        return getPlayerByDirection(banker)
    }

    fun currentPlayerIsBanker(current: Player): Boolean {
        return current == getBanker()
    }

    fun playerIsBanker(player: Player): Boolean {
        return player.direction == banker
    }

    fun updatePlayerName(player: Player, name: String) {
        val playerList = mutableListOf<Player>()
        for (i in players) {
            if (i == player) playerList.add(player.copy(name = name))
            else playerList.add(i)
        }
        players = playerList
        onDatabaseEvent(MajhongDatabaseEvent.UpsertPlayer(player.copy(name = name)))
    }

    private fun updatePlayerScore(player: Player, score: Int) {
        val playerList = mutableListOf<Player>()
        for (i in players) {
            if (i == player) playerList.add(player.copy(score = player.score + score))
            else playerList.add(i)
        }
        players = playerList
        onDatabaseEvent(MajhongDatabaseEvent.UpsertPlayer(player.copy(score = player.score + score)))
    }

    fun isAllPlayerNamed(): Boolean {
        for (i in 0 until 4) {
            if (getPlayerByDirection(i).name == "") {
                return false
            }
        }
        return true
    }

    fun draw() {
        if (drawToContinue) updateContinueToBank()
        else updateNextBanker()
    }

    private fun updateNextBanker() {
        banker = (banker + 1) % 4
        continueToBank = 0
        updateWind()
    }

    private fun updateContinueToBank() {
        continueToBank += 1
        onDatabaseEvent(MajhongDatabaseEvent.UpsertMajhongDatabase)
    }

    private fun updateWind() {
        wind = (wind + 1) % 4
        if (wind == 0) {
            updateRound()
        }
        onDatabaseEvent(MajhongDatabaseEvent.UpsertMajhongDatabase)
    }

    private fun updateRound() {
        round = (round + 1) % 4
    }

    fun calculateTotal(
        currentPlayer: Player, selectedPlayer: Player, numberOfTai: Int
    ): Int {
        val currentIsBanker =
            currentPlayerIsBanker(currentPlayer) || playerIsBanker(selectedPlayer)
        val selfDraw = currentPlayer == selectedPlayer
        val selfDrawAndNotBanker = selfDraw && !currentIsBanker
        return (baseTai + tai * numberOfTai + currentIsBanker.toInt() * tai * (2 * continueToBank + 1)) * (1 + selfDraw.toInt() * 2) + selfDrawAndNotBanker.toInt() * tai * (2 * continueToBank + 1)
    }

    fun updateScore(
        currentPlayer: Player, selectedPlayer: Player, numberOfTai: Int
    ) {
        val isBanker =
            playerIsBanker(selectedPlayer) || currentPlayerIsBanker(currentPlayer)
        val selfDraw = currentPlayer == selectedPlayer
        val selfDrawAndNotBanker = selfDraw && !isBanker
        val currentTotal =
            (baseTai + tai * numberOfTai + isBanker.toInt() * tai * (2 * continueToBank + 1)) * (1 + selfDraw.toInt() * 2) + selfDrawAndNotBanker.toInt() * tai * (2 * continueToBank + 1)
        if (selfDrawAndNotBanker) {
            for (i in players) {
                if (currentPlayer != i && i.direction != -1) {
                    if (i != getBanker()) {
                        updatePlayerScore(i, -(baseTai + tai * numberOfTai))
                    } else {
                        updatePlayerScore(
                            i, -(baseTai + tai * numberOfTai + tai * (2 * continueToBank + 1))
                        )
                    }
                }
            }
        } else if (selfDraw) {
            for (i in players) {
                if (currentPlayer != i && i.direction != -1) {
                    updatePlayerScore(
                        i, -(baseTai + tai * numberOfTai + tai * (2 * continueToBank + 1))
                    )
                }
            }
        } else if (isBanker) {
            updatePlayerScore(
                selectedPlayer, -(baseTai + tai * numberOfTai + tai * (2 * continueToBank + 1))
            )
        } else {
            updatePlayerScore(selectedPlayer, -(baseTai + tai * numberOfTai))
        }
        updatePlayerScore(currentPlayer, currentTotal)
        if (playerIsBanker(currentPlayer)) {
            updateContinueToBank()
        } else {
            updateNextBanker()
        }
    }
}