package com.example.majhong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import com.example.majhong.ui.BottomNavigationBar
import com.example.majhong.ui.CountScreen
import com.example.majhong.ui.GameScreen
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
                return MajhongViewModel(db.playerDao, db.majhongDao, db.majhongHistoryDao) as T
            }
        }
    })

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            val pageState = rememberPagerState(initialPage = 0)
//            db.clearAllTables()
            MajhongTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(snackBarHostState) { Snackbar { Text(text = "請先加入所有玩家") } }
                    },
                    bottomBar = { BottomNavigationBar(pageState) }
                ) { padding ->
                    HorizontalPager(pageCount = 2, state = pageState) { page ->
                        Column(modifier = Modifier.padding(padding)) {
                            if (page == 0) {
                                GameScreen(
                                    onEvent = majhongViewModel::onEvent,
                                    round = majhongViewModel.directions[majhongViewModel.round],
                                    wind = majhongViewModel.directions[majhongViewModel.wind],
                                    currentPlayerIsBanker = { current ->
                                        majhongViewModel.currentPlayerIsBanker(current)
                                    },
                                    selectedPlayerIsBanker = { selected ->
                                        majhongViewModel.playerIsBanker(selected)
                                    },
                                    continueToBank = { majhongViewModel.continueToBank },
                                    selectedPlayer = { index ->
                                        majhongViewModel.getPlayerByDirection(index)
                                    },
                                    baseTai = { majhongViewModel.baseTai },
                                    tai = { majhongViewModel.tai },
                                    isAllPlayerNamed = { majhongViewModel.isAllPlayerNamed() },
                                    calculateTotal =
                                    { current, selected, numberOfTai ->
                                        majhongViewModel.calculateTotal(
                                            current,
                                            selected,
                                            numberOfTai
                                        )
                                    },
                                    requiredAllPlayerName = {
                                        scope.launch {
                                            snackBarHostState.showSnackbar(
                                                message = "請先加入所有玩家",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    },
                                    isNameRepeated = { name ->
                                        majhongViewModel.isNameRepeated(name)
                                    },
                                    players = { majhongViewModel.players },
                                    bankerIndex = { majhongViewModel.banker },
                                    drawToContinue = { majhongViewModel.drawToContinue },
                                    newToClearPlayer = { majhongViewModel.newToClearPlayer }
                                )
                            } else if (page == 1) {
                                CountScreen(playerList = { majhongViewModel.players })
                            }
                        }
                    }
                }
            }
        }
    }
}