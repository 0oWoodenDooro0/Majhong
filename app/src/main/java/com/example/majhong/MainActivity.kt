package com.example.majhong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.majhong.database.MajhongDatabase
import com.example.majhong.database.MajhongDatabaseEvent
import com.example.majhong.database.Player
import com.example.majhong.ui.MainScreen
import com.example.majhong.ui.MainToolBar
import com.example.majhong.ui.theme.MajhongTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext, MajhongDatabase::class.java, "player.db"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Suppress("UNCHECKED_CAST")
    private val majhongViewModel by viewModels<MajhongViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MajhongViewModel(db.playerDao, db.majhongDao) as T
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
//            db.clearAllTables()
            MajhongTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(snackBarHostState) { Snackbar { Text(text = "請先加入所有玩家") } }
                    },
                    topBar = {
                        MainToolBar(
                            baseTai = { majhongViewModel.baseTai },
                            tai = { majhongViewModel.tai },
                            drawToContinue = { majhongViewModel.drawToContinue },
                            newToClearPlayer = { majhongViewModel.newToClearPlayer },
                            onModifyRules = { baseTai, tai, drawToContinue, newToClearPlayer ->
                                majhongViewModel.onDatabaseEvent(
                                    MajhongDatabaseEvent.UpsertNewMajhongDatabase(
                                        baseTai,
                                        tai,
                                        drawToContinue,
                                        newToClearPlayer
                                    )
                                )
                            },
                            AddPlayer = { name ->
                                majhongViewModel.onDatabaseEvent(
                                    MajhongDatabaseEvent.UpsertPlayer(
                                        Player(name)
                                    )
                                )
                            },
                            players = majhongViewModel.playerStates,
                            swapPlayer = { player1, player2 ->
                                majhongViewModel.onDatabaseEvent(
                                    MajhongDatabaseEvent.SwapPlayer(
                                        player1,
                                        player2
                                    )
                                )
                            }
                        )
                    }
                ) { padding ->
                    Column(modifier = Modifier.padding(padding)) {
                        MainScreen(
                            round = majhongViewModel.directions[majhongViewModel.round],
                            wind = majhongViewModel.directions[majhongViewModel.wind],
                            players = majhongViewModel.playerStates,
                            currentPlayerIsBanker = { current ->
                                majhongViewModel.currentPlayerIsBanker(current)
                            },
                            selectedPlayerIsBanker = { selected ->
                                majhongViewModel.selectedPlayerIsBanker(selected)
                            },
                            continueToBank = { majhongViewModel.continueToBank },
                            selectedPlayerState = { index ->
                                majhongViewModel.playerStates[index]
                            },
                            baseTai = { majhongViewModel.baseTai },
                            tai = { majhongViewModel.tai },
                            isAllPlayerNamed = { majhongViewModel.isAllPlayerNamed() },
                            calculateTotal =
                            { current, selected, numberOfTai ->
                                majhongViewModel.calculateTotal(current, selected, numberOfTai)
                            },
                            updateName = { current, playerName ->
                                majhongViewModel.updatePlayerName(current, playerName)
                            },
                            updateScore =
                            { current, selected, numberOfTai ->
                                majhongViewModel.updateScore(current, selected, numberOfTai)
                            },
                            draw = { majhongViewModel.draw() },
                            requiredAllPlayerName = {
                                scope.launch {
                                    snackBarHostState.showSnackbar(
                                        message = "請先加入所有玩家",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}