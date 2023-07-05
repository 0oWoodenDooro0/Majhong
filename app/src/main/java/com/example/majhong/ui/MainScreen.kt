package com.example.majhong.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.majhong.database.Player

@Composable
fun MainScreen(
    round: String,
    wind: String,
    currentPlayerIsBanker: (Player) -> Boolean,
    selectedPlayerIsBanker: (Player) -> Boolean,
    continueToBank: () -> Int,
    selectedPlayer: (Int) -> Player,
    baseTai: () -> Int,
    tai: () -> Int,
    isAllPlayerNamed: () -> Boolean,
    calculateTotal: (Player, Player, Int) -> Int,
    updateName: (Player, String, Int) -> Unit,
    updateScore: (Player, Player, Int) -> Unit,
    draw: () -> Unit,
    requiredAllPlayerName: () -> Unit,
    isNameRepeated: (String) -> Boolean,
    players: () -> List<Player>,
    resetBanker: (Int, Boolean, Boolean) -> Unit,
    bankerIndex: () -> Int
) {
    var showSettleDialog by remember { mutableStateOf(false) }
    var showBankerDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.padding(15.dp)) {
            FilledTonalButton(onClick = {
                if (isAllPlayerNamed()) {
                    showBankerDialog = true
                } else {
                    requiredAllPlayerName()
                }
            }) {
                Text(
                    text = "${round}圈${wind}風",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            PlayerCard(
                Modifier.weight(1f),
                selectedPlayer(2),
                currentPlayerIsBanker,
                selectedPlayerIsBanker,
                continueToBank,
                selectedPlayer,
                baseTai,
                tai,
                isAllPlayerNamed,
                calculateTotal,
                { current, playerName ->
                    updateName(current, playerName, 2)
                },
                updateScore,
                requiredAllPlayerName,
                isNameRepeated
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
        ) {
            PlayerCard(
                Modifier.weight(1f),
                selectedPlayer(3),
                currentPlayerIsBanker,
                selectedPlayerIsBanker,
                continueToBank,
                selectedPlayer,
                baseTai,
                tai,
                isAllPlayerNamed,
                calculateTotal,
                { current, playerName ->
                    updateName(current, playerName, 3)
                },
                updateScore,
                requiredAllPlayerName,
                isNameRepeated
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = draw) {
                    Text(text = "流局")
                }
                Button(onClick = {
                    if (isAllPlayerNamed()) {
                        showSettleDialog = true
                    } else {
                        requiredAllPlayerName()
                    }
                }) {
                    Text(text = "結算")
                }
            }
            PlayerCard(
                Modifier.weight(1f),
                selectedPlayer(1),
                currentPlayerIsBanker,
                selectedPlayerIsBanker,
                continueToBank,
                selectedPlayer,
                baseTai,
                tai,
                isAllPlayerNamed,
                calculateTotal,
                { current, playerName ->
                    updateName(current, playerName, 1)
                },
                updateScore,
                requiredAllPlayerName,
                isNameRepeated
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            PlayerCard(
                Modifier.weight(1f),
                selectedPlayer(0),
                currentPlayerIsBanker,
                selectedPlayerIsBanker,
                continueToBank,
                selectedPlayer,
                baseTai,
                tai,
                isAllPlayerNamed,
                calculateTotal,
                { current, playerName ->
                    updateName(current, playerName, 0)
                },
                updateScore,
                requiredAllPlayerName,
                isNameRepeated
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
    if (showBankerDialog) {
        BankerDialog(
            onDismiss = { showBankerDialog = false },
            getPlayerByDirection = selectedPlayer,
            resetBanker = { index, resetContinue, resetRoundWind ->
                resetBanker(index, resetContinue, resetRoundWind)
                showBankerDialog = false
            },
            bankerIndex = bankerIndex
        )
    }
    if (showSettleDialog) {
        SettleDialog(onDismiss = { showSettleDialog = false }, players = players)
    }
}