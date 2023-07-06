package com.example.majhong

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.majhong.database.Majhong
import com.example.majhong.database.MajhongDao
import com.example.majhong.database.MajhongDatabaseEvent
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
                            newToClearPlayer,
                            historySize
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
                            event.newToClearPlayer,
                            0
                        )
                    )
                    if (event.newToClearPlayer) playerDao.deleteAllPlayer()
                    else playerDao.setAllPlayerScoreToZero()
                    historySize = 0
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
                    onDatabaseEvent(MajhongDatabaseEvent.InitMajhongAndPlayerDatabase)
                }
            }

            is MajhongDatabaseEvent.UpsertMajhongHistory -> {
                viewModelScope.launch {
                    historySize += 1
                    majhongHistoryDao.upsertMajhongHistory(
                        MajhongHistory(
                            event.players[0].id,
                            event.players[0].score,
                            event.players[1].id,
                            event.players[1].score,
                            event.players[2].id,
                            event.players[2].score,
                            event.players[3].id,
                            event.players[3].score,
                            banker,
                            continueToBank,
                            round,
                            wind,
                            historySize
                        )
                    )
                    onDatabaseEvent(MajhongDatabaseEvent.UpsertMajhongDatabase)
                }
            }

            is MajhongDatabaseEvent.DeleteAllMajhongHistory -> {
                viewModelScope.launch {
                    majhongHistoryDao.deleteAllMajhongHistory()
                }
            }

            is MajhongDatabaseEvent.Undo -> {
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
                    onDatabaseEvent(MajhongDatabaseEvent.UpsertMajhongDatabase)
                    onDatabaseEvent(MajhongDatabaseEvent.GetAllPlayer)
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

    fun resetBanker(bankerIndex: Int, resetContinue: Boolean, resetRoundWind: Boolean) {
        banker = bankerIndex
        if (resetContinue) continueToBank = 0
        if (resetRoundWind) {
            wind = 0
            round = 0
        }
        onDatabaseEvent(MajhongDatabaseEvent.UpsertMajhongDatabase)
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

    fun updatePlayerName(player: Player, name: String, direction: Int = -1) {
        val playerList = mutableListOf<Player>()
        for (i in players) {
            if (i == player) playerList.add(player.copy(name = name, direction = direction))
            else playerList.add(i)
        }
        players = playerList
        onDatabaseEvent(
            MajhongDatabaseEvent.UpsertPlayer(
                player.copy(
                    name = name,
                    direction = direction
                )
            )
        )
        onDatabaseEvent(MajhongDatabaseEvent.GetAllPlayer)
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
        val playerList = mutableListOf(Player(), Player(), Player(), Player())
        if (selfDrawAndNotBanker) {
            for (i in players) {
                if (currentPlayer != i && i.direction != -1) {
                    val score = if (i != getBanker()) {
                        -(baseTai + tai * numberOfTai)
                    } else {
                        -(baseTai + tai * numberOfTai + tai * (2 * continueToBank + 1))
                    }
                    updatePlayerScore(i, score)
                    playerList[i.direction] = Player(id = i.id, score = score)
                }
            }
        } else if (selfDraw) {
            for (i in players) {
                if (currentPlayer != i && i.direction != -1) {
                    val score = -(baseTai + tai * numberOfTai + tai * (2 * continueToBank + 1))
                    updatePlayerScore(i, score)
                    playerList[i.direction] = Player(id = i.id, score = score)
                }
            }
        } else if (isBanker) {
            val score = -(baseTai + tai * numberOfTai + tai * (2 * continueToBank + 1))
            updatePlayerScore(selectedPlayer, score)
            playerList[selectedPlayer.direction] =
                Player(id = selectedPlayer.id, score = score)
        } else {
            val score = -(baseTai + tai * numberOfTai)
            updatePlayerScore(selectedPlayer, score)
            playerList[selectedPlayer.direction] =
                Player(id = selectedPlayer.id, score = score)
        }
        updatePlayerScore(currentPlayer, currentTotal)
        playerList[currentPlayer.direction] =
            Player(id = currentPlayer.id, score = currentTotal)
        onDatabaseEvent(MajhongDatabaseEvent.UpsertMajhongHistory(playerList))
        if (playerIsBanker(currentPlayer)) {
            updateContinueToBank()
        } else {
            updateNextBanker()
        }
    }
}