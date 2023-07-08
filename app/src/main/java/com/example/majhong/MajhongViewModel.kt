package com.example.majhong

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.majhong.database.Majhong
import com.example.majhong.database.MajhongDao
import com.example.majhong.database.MajhongEvent
import com.example.majhong.database.MajhongHistory
import com.example.majhong.database.MajhongHistoryDao
import com.example.majhong.database.Player
import com.example.majhong.database.PlayerDao
import kotlinx.coroutines.launch

fun Boolean.toInt() = if (this) 1 else 0

class MajhongViewModel(
    private val playerDao: PlayerDao,
    private val majhongDao: MajhongDao,
    private val majhongHistoryDao: MajhongHistoryDao
) : ViewModel() {

    var players by mutableStateOf(listOf(Player(), Player(), Player(), Player()))

    private var historySize by mutableStateOf(0)

    val directions = listOf("東", "南", "西", "北")

    var banker by mutableStateOf(0)

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

    fun onEvent(event: MajhongEvent) {
        when (event) {
            is MajhongEvent.InitMajhongAndPlayer -> {
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
                        historySize = majhong.historySize
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
                                newToClearPlayer = false,
                                historySize = 0
                            )
                        )
                    }
                }
            }

            is MajhongEvent.UpsertMajhong -> {
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
                            newToClearPlayer,
                            historySize
                        )
                    )
                }
            }

            is MajhongEvent.ModifyRules -> {
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
                            event.newToClearPlayer,
                            0
                        )
                    )
                    if (event.newToClearPlayer) playerDao.deleteAllPlayer()
                    else playerDao.setAllPlayerScoreToZero()
                    historySize = 0
                    onEvent(MajhongEvent.InitMajhongAndPlayer)
                    majhongHistoryDao.deleteAllMajhongHistory()
                }
            }

            is MajhongEvent.SwapPlayer -> {
                viewModelScope.launch {
                    playerDao.updatePlayerDirectionById(
                        event.player2.direction, event.player1.id
                    )
                    playerDao.updatePlayerDirectionById(
                        event.player1.direction, event.player2.id
                    )
                    onEvent(MajhongEvent.InitMajhongAndPlayer)
                }
            }

            is MajhongEvent.Undo -> {
                viewModelScope.launch {
                    val history =
                        majhongHistoryDao.findMajhongHistoryById(historySize) ?: return@launch
                    if (history.player1Id != 0) {
                        val player = playerDao.getPlayerById(history.player1Id)
                        playerDao.updatePlayerScoreById(
                            score = player.score - history.score1,
                            id = history.player1Id
                        )
                    }
                    if (history.player2Id != 0) {
                        val player = playerDao.getPlayerById(history.player2Id)
                        playerDao.updatePlayerScoreById(
                            score = player.score - history.score2,
                            id = history.player2Id
                        )
                    }
                    if (history.player3Id != 0) {
                        val player = playerDao.getPlayerById(history.player3Id)
                        playerDao.updatePlayerScoreById(
                            score = player.score - history.score3,
                            id = history.player3Id
                        )
                    }
                    if (history.player4Id != 0) {
                        val player = playerDao.getPlayerById(history.player4Id)
                        playerDao.updatePlayerScoreById(
                            score = player.score - history.score4,
                            id = history.player4Id
                        )
                    }
                    banker = history.banker
                    continueToBank = history.continueToBank
                    wind = history.wind
                    round = history.round
                    majhongHistoryDao.deleteMajhongHistoryById(historySize)
                    historySize -= 1
                    onEvent(MajhongEvent.UpsertMajhong)
                    players = playerDao.getAllPlayer()
                }
            }

            is MajhongEvent.AddNewPlayer -> {
                viewModelScope.launch {
                    playerDao.upsertPlayer(
                        event.player.copy(
                            name = event.name,
                            direction = event.direction
                        )
                    )
                    players = playerDao.getAllPlayer()
                }
            }

            is MajhongEvent.UpdateScore -> {
                val isBanker =
                    playerIsBanker(event.selectedPlayer) || currentPlayerIsBanker(event.currentPlayer)
                val selfDraw = event.currentPlayer == event.selectedPlayer
                val selfDrawAndNotBanker = selfDraw && !isBanker
                val currentTotal =
                    (baseTai + tai * event.numberOfTai + isBanker.toInt() * tai * (2 * continueToBank + 1)) * (1 + selfDraw.toInt() * 2) + selfDrawAndNotBanker.toInt() * tai * (2 * continueToBank + 1)
                val playerList = mutableListOf(Player(), Player(), Player(), Player())
                if (selfDrawAndNotBanker) {
                    for (player in players) {
                        if (event.currentPlayer != player && player.direction != -1) {
                            val score = if (player != getBanker()) {
                                -(baseTai + tai * event.numberOfTai)
                            } else {
                                -(baseTai + tai * event.numberOfTai + tai * (2 * continueToBank + 1))
                            }
                            playerList[player.direction] = Player(id = player.id, score = score)
                            viewModelScope.launch {
                                playerDao.upsertPlayer(player.copy(score = player.score + score))
                            }
                        }
                    }
                } else if (selfDraw) {
                    for (player in players) {
                        if (event.currentPlayer != player && player.direction != -1) {
                            val score =
                                -(baseTai + tai * event.numberOfTai + tai * (2 * continueToBank + 1))
                            playerList[player.direction] = Player(id = player.id, score = score)
                            viewModelScope.launch {
                                playerDao.upsertPlayer(player.copy(score = player.score + score))
                            }
                        }
                    }
                } else if (isBanker) {
                    val score =
                        -(baseTai + tai * event.numberOfTai + tai * (2 * continueToBank + 1))
                    playerList[event.selectedPlayer.direction] =
                        Player(id = event.selectedPlayer.id, score = score)
                    viewModelScope.launch {
                        playerDao.upsertPlayer(event.selectedPlayer.copy(score = event.selectedPlayer.score + score))
                    }
                } else {
                    val score = -(baseTai + tai * event.numberOfTai)
                    playerList[event.selectedPlayer.direction] =
                        Player(id = event.selectedPlayer.id, score = score)
                    viewModelScope.launch {
                        playerDao.upsertPlayer(event.selectedPlayer.copy(score = event.selectedPlayer.score + score))
                    }
                }
                playerList[event.currentPlayer.direction] =
                    Player(id = event.currentPlayer.id, score = currentTotal)
                viewModelScope.launch {
                    playerDao.upsertPlayer(event.currentPlayer.copy(score = event.currentPlayer.score + currentTotal))
                    historySize += 1
                    majhongHistoryDao.upsertMajhongHistory(
                        MajhongHistory(
                            playerList[0].id,
                            playerList[0].score,
                            playerList[1].id,
                            playerList[1].score,
                            playerList[2].id,
                            playerList[2].score,
                            playerList[3].id,
                            playerList[3].score,
                            banker,
                            continueToBank,
                            round,
                            wind,
                            historySize
                        )
                    )
                }
                onEvent(MajhongEvent.UpsertMajhong)
                if (playerIsBanker(event.currentPlayer)) {
                    updateContinueToBank()
                } else {
                    updateNextBanker()
                }
                viewModelScope.launch {
                    players = playerDao.getAllPlayer()
                }
            }

            is MajhongEvent.ResetBanker -> {
                banker = event.bankerIndex
                if (event.resetContinue) continueToBank = 0
                if (event.resetRoundWind) {
                    wind = 0
                    round = 0
                }
                onEvent(MajhongEvent.UpsertMajhong)
            }

            is MajhongEvent.Draw -> {
                if (drawToContinue) updateContinueToBank()
                else updateNextBanker()
            }
        }
    }

    init {
        onEvent(MajhongEvent.InitMajhongAndPlayer)
    }

    fun getPlayerByDirection(direction: Int): Player {
        for (i in players) {
            if (i.direction == direction) return i
        }
        return Player()
    }

    fun isNameRepeated(name: String): Boolean {
        for (i in players) {
            if (i.name == name) return true
        }
        return false
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

    fun isAllPlayerNamed(): Boolean {
        for (i in 0 until 4) {
            if (getPlayerByDirection(i).name == "") {
                return false
            }
        }
        return true
    }

    private fun updateNextBanker() {
        banker = (banker + 1) % 4
        continueToBank = 0
        updateWind()
    }

    private fun updateContinueToBank() {
        continueToBank += 1
        onEvent(MajhongEvent.UpsertMajhong)
    }

    private fun updateWind() {
        wind = (wind + 1) % 4
        if (wind == 0) {
            updateRound()
        }
        onEvent(MajhongEvent.UpsertMajhong)
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
}