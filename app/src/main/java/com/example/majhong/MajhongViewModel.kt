package com.example.majhong

import androidx.compose.runtime.MutableState
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
    private val playerDao: PlayerDao,
    private val majhongDao: MajhongDao
) : ViewModel() {

    private val _playerStates =
        mutableListOf(PlayerState(""), PlayerState(""), PlayerState(""), PlayerState(""))
    val playerStates: List<PlayerState> = _playerStates

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

    fun onDatabaseEvent(event: MajhongDatabaseEvent) {
        when (event) {
            is MajhongDatabaseEvent.InitMajhongAndPlayerDatabase -> {
                viewModelScope.launch {
                    for (i in 0 until 4) {
                        val player = playerDao.getPlayerByDirection(i)
                        if (player != null) {
                            _playerStates[i].name = player.name
                            _playerStates[i].score.value = player.score
                        } else {
                            _playerStates[i].name = ""
                            _playerStates[i].score.value = 0
                        }
                    }
                    val majhong = majhongDao.getMajhongById()
                    if (majhong != null) {
                        banker = majhong.banker
                        continueToBank = majhong.continueToBank
                        round = majhong.round
                        wind = majhong.wind
                        baseTai = majhong.baseTai
                        tai = majhong.tai
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
                    playerDao.updatePlayerScore(event.score, event.direction)
                }
            }

            is MajhongDatabaseEvent.UpsertMajhongDatabase -> {
                viewModelScope.launch {
                    majhongDao.upsertMajhong(event.majhong)
                }
            }

            is MajhongDatabaseEvent.UpsertNewMajhongDatabase -> {
                viewModelScope.launch {
                    playerDao.deleteAllPlayer()
                    majhongDao.upsertMajhong(Majhong(0, 0, 0, 0, event.baseTai, event.tai))
                    onDatabaseEvent(MajhongDatabaseEvent.InitMajhongAndPlayerDatabase)
                }
            }
        }
    }

    init {
        onDatabaseEvent(MajhongDatabaseEvent.InitMajhongAndPlayerDatabase)
    }

    private fun getBanker(): PlayerState {
        return _playerStates[banker]
    }

    fun currentPlayerIsBanker(current: PlayerState): Boolean {
        return current == getBanker()
    }

    fun selectedPlayerIsBanker(selected: PlayerState): Boolean {
        return selected == getBanker()
    }

    fun updatePlayerName(playerState: PlayerState, name: String) {
        val index = _playerStates.indexOf(playerState)
        _playerStates[index].name = name
        onDatabaseEvent(MajhongDatabaseEvent.UpsertPlayer(Player(name, 0, index)))
    }

    private fun updatePlayerScore(playerState: PlayerState, score: Int) {
        val index = _playerStates.indexOf(playerState)
        _playerStates[index].score.value += score
        onDatabaseEvent(
            MajhongDatabaseEvent.UpdatePlayerScore(
                _playerStates[index].score.value,
                index
            )
        )
    }

    fun isAllPlayerNamed(): Boolean {
        for (i in 0 until _playerStates.size - 1) {
            if (playerStates[i].name == "") return false
        }
        return true
    }

    fun draw() {
        updateContinueToBank()
    }

    private fun updateNextBanker() {
        val index = playerStates.indexOf(getBanker())
        banker = (index + 1) % 4
        continueToBank = 0
        updateWind()
    }

    private fun updateContinueToBank() {
        continueToBank += 1
        onDatabaseEvent(
            MajhongDatabaseEvent.UpsertMajhongDatabase(
                Majhong(
                    banker,
                    continueToBank,
                    round,
                    wind,
                    baseTai,
                    tai
                )
            )
        )
    }

    private fun updateWind() {
        wind = (wind + 1) % 4
        if (wind == 0) {
            updateRound()
        }
        onDatabaseEvent(
            MajhongDatabaseEvent.UpsertMajhongDatabase(
                Majhong(
                    banker,
                    continueToBank,
                    round,
                    wind,
                    baseTai,
                    tai
                )
            )
        )
    }

    private fun updateRound() {
        round = (round + 1) % 4
    }

    fun calculateTotal(
        currentPlayerState: PlayerState,
        selectedPlayerState: PlayerState,
        numberOfTai: Int
    ): Int {
        val currentIsBanker =
            currentPlayerIsBanker(currentPlayerState) || selectedPlayerIsBanker(selectedPlayerState)
        val selfDraw = currentPlayerState == selectedPlayerState
        val selfDrawAndNotBanker = selfDraw && !currentIsBanker
        return (baseTai + tai * numberOfTai + currentIsBanker.toInt() * tai * (2 * continueToBank + 1)) * (1 + selfDraw.toInt() * 2) + selfDrawAndNotBanker.toInt() * tai * (2 * continueToBank + 1)
    }

    fun updateScore(
        currentPlayerState: PlayerState,
        selectedPlayerState: PlayerState,
        numberOfTai: Int
    ) {
        val isBanker = selectedPlayerIsBanker(selectedPlayerState) || currentPlayerIsBanker(currentPlayerState)
        val selfDraw = currentPlayerState == selectedPlayerState
        val selfDrawAndNotBanker = selfDraw && !isBanker
        val currentTotal =
            (baseTai + tai * numberOfTai + isBanker.toInt() * tai * (2 * continueToBank + 1)) * (1 + selfDraw.toInt() * 2) + selfDrawAndNotBanker.toInt() * tai * (2 * continueToBank + 1)
        if (selfDrawAndNotBanker) {
            for (i in playerStates) {
                if (currentPlayerState != i) {
                    if (i != getBanker()) {
                        updatePlayerScore(i, -(baseTai + tai * numberOfTai))
                    } else {
                        updatePlayerScore(
                            i,
                            -(baseTai + tai * numberOfTai + tai * (2 * continueToBank + 1))
                        )
                    }
                }
            }
        } else if (selfDraw) {
            for (i in playerStates) {
                if (currentPlayerState != i) {
                    updatePlayerScore(
                        i,
                        -(baseTai + tai * numberOfTai + tai * (2 * continueToBank + 1))
                    )
                }
            }
        } else if (isBanker) {
            updatePlayerScore(
                selectedPlayerState,
                -(baseTai + tai * numberOfTai + tai * (2 * continueToBank + 1))
            )
        } else {
            updatePlayerScore(selectedPlayerState, -(baseTai + tai * numberOfTai))
        }
        updatePlayerScore(currentPlayerState, currentTotal)
        if (selectedPlayerIsBanker(selectedPlayerState)) {
            updateContinueToBank()
        } else {
            updateNextBanker()
        }
    }
}

data class PlayerState(var name: String = "", var score: MutableState<Int> = mutableStateOf(0))