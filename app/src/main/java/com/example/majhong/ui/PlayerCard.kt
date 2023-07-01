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
import com.example.majhong.PlayerState
import com.example.majhong.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerCard(
    modifier: Modifier,
    currentPlayerState: PlayerState,
    currentPlayerIsBanker: (PlayerState) -> Boolean,
    selectedPlayerIsBanker: (PlayerState) -> Boolean,
    continueToBank: () -> Int,
    selectedPlayerState: (Int) -> PlayerState,
    baseTai: () -> Int,
    tai: () -> Int,
    isAllPlayerNamed: () -> Boolean,
    calculateTotal: (PlayerState, PlayerState, Int) -> Int,
    updateName: (PlayerState, String) -> Unit,
    updateScore: (PlayerState, PlayerState, Int) -> Unit,
    requiredAllPlayerName: () -> Unit
) {
    var showWinDialog by remember { mutableStateOf(false) }
    var showAddNameDialog by remember { mutableStateOf(false) }
    if (showWinDialog) {
        WinDialog(onDismiss = {
            showWinDialog = false
        },
            currentPlayerIsBanker = currentPlayerIsBanker,
            selectedPlayerIsBanker = selectedPlayerIsBanker,
            continueToBank = continueToBank,
            currentPlayerState = currentPlayerState,
            selectedPlayerState = selectedPlayerState,
            baseTai = baseTai,
            tai = tai,
            calculateTotal = { selected, numberOfTai ->
                calculateTotal(currentPlayerState, selected, numberOfTai)
            },
            buttonOnClick = { selected, numberOfTai ->
                updateScore(currentPlayerState, selected, numberOfTai)
                showWinDialog = false
            })
    }
    if (showAddNameDialog) {
        AddNameDialog(onDismiss = {
            showAddNameDialog = false
        }, buttonOnClick = { playerName ->
            updateName(currentPlayerState, playerName)
            showAddNameDialog = false
        })
    }
    Column(modifier = modifier) {
        Card(shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            elevation = CardDefaults.cardElevation(5.dp),
            onClick = {
                if (currentPlayerState.name == "") {
                    showAddNameDialog = true
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
                if (currentPlayerState.name != "") {
                    AutoSizeText(
                        text = currentPlayerState.name,
                        maxTextSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                    AutoSizeText(
                        text = currentPlayerState.score.value.toString(),
                        maxTextSize = 32.sp,
                        textAlign = TextAlign.Center
                    )
                    if (currentPlayerIsBanker(currentPlayerState) && continueToBank() == 0) {
                        Text(
                            text = "莊",
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(5.dp),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onTertiary,
                            textAlign = TextAlign.Center,
                        )
                    } else if (currentPlayerIsBanker(currentPlayerState)) {
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