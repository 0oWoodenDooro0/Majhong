package com.example.majhong.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.majhong.R
import com.example.majhong.database.Player


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerCard(
    modifier: Modifier,
    currentPlayer: Player,
    currentPlayerIsBanker: (Player) -> Boolean,
    selectedPlayerIsBanker: (Player) -> Boolean,
    continueToBank: () -> Int,
    selectedPlayer: (Int) -> Player,
    baseTai: () -> Int,
    tai: () -> Int,
    isAllPlayerNamed: () -> Boolean,
    calculateTotal: (Player, Player, Int) -> Int,
    updateName: (Player, String) -> Unit,
    updateScore: (Player, Player, Int) -> Unit,
    requiredAllPlayerName: () -> Unit
) {
    var showWinDialog by remember { mutableStateOf(false) }
    var showAddPlayerDialog by remember { mutableStateOf(false) }
    if (showWinDialog) {
        WinDialog(onDismiss = {
            showWinDialog = false
        },
            currentPlayerIsBanker = currentPlayerIsBanker,
            selectedPlayerIsBanker = selectedPlayerIsBanker,
            continueToBank = continueToBank,
            currentPlayer = currentPlayer,
            selectedPlayer = selectedPlayer,
            baseTai = baseTai,
            tai = tai,
            calculateTotal = { selected, numberOfTai ->
                calculateTotal(currentPlayer, selected, numberOfTai)
            },
            buttonOnClick = { selected, numberOfTai ->
                updateScore(currentPlayer, selected, numberOfTai)
                showWinDialog = false
            })
    }
    if (showAddPlayerDialog) {
        AddPlayerDialog(onDismiss = {
            showAddPlayerDialog = false
        }, buttonOnClick = { playerName ->
            updateName(currentPlayer, playerName)
            showAddPlayerDialog = false
        })
    }
    Column(modifier = modifier) {
        Card(shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            elevation = CardDefaults.cardElevation(5.dp),
            onClick = {
                if (currentPlayer.name == "") {
                    showAddPlayerDialog = true
                } else if (!isAllPlayerNamed()) {
                    requiredAllPlayerName()
                } else {
                    showWinDialog = true
                }
            }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (currentPlayer.name != "") {
                    AutoSizeText(
                        text = currentPlayer.name,
                        maxTextSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                    AutoSizeText(
                        text = currentPlayer.score.toString(),
                        maxTextSize = 32.sp,
                        textAlign = TextAlign.Center
                    )
                    if (currentPlayerIsBanker(currentPlayer) && continueToBank() == 0) {
                        Text(
                            text = "莊",
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.secondary,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(5.dp),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSecondary,
                            textAlign = TextAlign.Center,
                        )
                    } else if (currentPlayerIsBanker(currentPlayer)) {
                        Text(
                            text = "連${continueToBank()}",
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(5.dp),
                            color = MaterialTheme.colorScheme.onTertiary,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_person_add_alt_1_24),
                        contentDescription = stringResource(id = R.string.person_add_content)
                    )
                }
            }
        }
    }
}