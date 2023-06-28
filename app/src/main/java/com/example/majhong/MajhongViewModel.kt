package com.example.majhong

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.majhong.database.Majhong
import com.example.majhong.database.MajhongDao
import com.example.majhong.database.MajhongEvent
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

    private val banker = mutableStateOf(0)

    var continueToBank = mutableStateOf(0)
        private set

    var round = mutableStateOf(0)
        private set

    var wind = mutableStateOf(0)
        private set

    var baseTai = mutableStateOf(30)
        private set

    var tai = mutableStateOf(10)
        private set

    private fun onEvent(event: MajhongEvent) {
        when (event) {
            is MajhongEvent.UpsertPlayer -> {
                viewModelScope.launch {
                    viewModelScope.launch {
                        playerDao.upsertPlayer(event.player)
                    }
                }
            }

            is MajhongEvent.UpdatePlayerScore -> {
                viewModelScope.launch {
                    playerDao.updatePlayerScore(event.score, event.direction)
                }
            }

            is MajhongEvent.UpsertMajhong -> {
                viewModelScope.launch {
                    majhongDao.upsertMajhong(event.majhong)
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            for (i in 0 until 4) {
                val player = playerDao.getPlayerByDirection(i)
                if (player != null) {
                    _playerStates[i].name = player.name
                    _playerStates[i].score.value = player.score
                }
            }
            val majhong = majhongDao.getMajhongById()
            if (majhong != null) {
                banker.value = majhong.banker
                continueToBank.value = majhong.continueToBank
                round.value = majhong.round
                wind.value = majhong.wind
                baseTai.value = majhong.baseTai
                tai.value = majhong.tai
            }
        }
    }

    fun getBanker(): PlayerState {
        return _playerStates[banker.value]
    }

    fun updatePlayerName(playerState: PlayerState, name: String) {
        val index = _playerStates.indexOf(playerState)
        _playerStates[index].name = name
        onEvent(MajhongEvent.UpsertPlayer(Player(name, 0, index)))
    }

    private fun updatePlayerScore(playerState: PlayerState, score: Int) {
        val index = _playerStates.indexOf(playerState)
        _playerStates[index].score.value += score
        onEvent(MajhongEvent.UpdatePlayerScore(_playerStates[index].score.value, index))
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
        banker.value = (index + 1) % 4
        continueToBank.value = 0
        updateWind()
    }

    private fun updateContinueToBank() {
        continueToBank.value += 1
        onEvent(
            MajhongEvent.UpsertMajhong(
                Majhong(
                    banker.value,
                    continueToBank.value,
                    round.value,
                    wind.value,
                    baseTai.value,
                    tai.value
                )
            )
        )
    }

    private fun updateWind() {
        wind.value = (wind.value + 1) % 4
        if (wind.value == 0) {
            updateRound()
        }
        onEvent(
            MajhongEvent.UpsertMajhong(
                Majhong(
                    banker.value,
                    continueToBank.value,
                    round.value,
                    wind.value,
                    baseTai.value,
                    tai.value
                )
            )
        )
    }

    private fun updateRound() {
        round.value = (round.value + 1) % 4
    }

    fun calculateTotal(
        currentPlayerState: PlayerState,
        selectedPlayerState: PlayerState,
        numberOfTai: Int
    ): Int {
        val currentIsBanker =
            currentPlayerState == getBanker() || selectedPlayerState == getBanker()
        val selfDraw = currentPlayerState == selectedPlayerState
        val selfDrawAndNotBanker = selfDraw && !currentIsBanker
        return (baseTai.value + tai.value * numberOfTai + currentIsBanker.toInt() * tai.value * (2 * continueToBank.value + 1)) * (1 + selfDraw.toInt() * 2) + selfDrawAndNotBanker.toInt() * tai.value * (2 * continueToBank.value + 1)
    }

    fun updateScore(
        currentPlayerState: PlayerState,
        selectedPlayerState: PlayerState,
        numberOfTai: Int
    ) {
        val selectedIsBanker = selectedPlayerState == getBanker()
        val currentIsBanker = currentPlayerState == getBanker()
        val isBanker = currentIsBanker || selectedIsBanker
        val selfDraw = currentPlayerState == selectedPlayerState
        val selfDrawAndNotBanker = selfDraw && !isBanker
        val currentTotal =
            (baseTai.value + tai.value * numberOfTai + isBanker.toInt() * tai.value * (2 * continueToBank.value + 1)) * (1 + selfDraw.toInt() * 2) + selfDrawAndNotBanker.toInt() * tai.value * (2 * continueToBank.value + 1)
        if (selfDrawAndNotBanker) {
            for (i in playerStates) {
                if (currentPlayerState != i) {
                    if (i != getBanker()) {
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
            for (i in playerStates) {
                if (currentPlayerState != i) {
                    updatePlayerScore(
                        i,
                        -(baseTai.value + tai.value * numberOfTai + tai.value * (2 * continueToBank.value + 1))
                    )
                }
            }
        } else if (isBanker) {
            updatePlayerScore(
                selectedPlayerState,
                -(baseTai.value + tai.value * numberOfTai + tai.value * (2 * continueToBank.value + 1))
            )
        } else {
            updatePlayerScore(selectedPlayerState, -(baseTai.value + tai.value * numberOfTai))
        }
        updatePlayerScore(currentPlayerState, currentTotal)
        if (currentIsBanker) {
            updateContinueToBank()
        } else {
            updateNextBanker()
        }
    }
}

data class PlayerState(var name: String = "", var score: MutableState<Int> = mutableStateOf(0))