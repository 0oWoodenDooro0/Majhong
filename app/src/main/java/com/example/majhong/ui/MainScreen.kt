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
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.majhong.PlayerState

@Composable
fun MainScreen(
    round: String,
    wind: String,
    players: List<PlayerState>,
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
    draw: () -> Unit,
    requiredAllPlayerName: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.padding(15.dp)) {
            ElevatedButton(onClick = { }) {
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
                players[2],
                currentPlayerIsBanker,
                selectedPlayerIsBanker,
                continueToBank,
                selectedPlayerState,
                baseTai,
                tai,
                isAllPlayerNamed,
                calculateTotal,
                updateName,
                updateScore,
                requiredAllPlayerName
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
                players[3],
                currentPlayerIsBanker,
                selectedPlayerIsBanker,
                continueToBank,
                selectedPlayerState,
                baseTai,
                tai,
                isAllPlayerNamed,
                calculateTotal,
                updateName,
                updateScore,
                requiredAllPlayerName
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
            }
            PlayerCard(
                Modifier.weight(1f),
                players[1],
                currentPlayerIsBanker,
                selectedPlayerIsBanker,
                continueToBank,
                selectedPlayerState,
                baseTai,
                tai,
                isAllPlayerNamed,
                calculateTotal,
                updateName,
                updateScore,
                requiredAllPlayerName
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
                players[0],
                currentPlayerIsBanker,
                selectedPlayerIsBanker,
                continueToBank,
                selectedPlayerState,
                baseTai,
                tai,
                isAllPlayerNamed,
                calculateTotal,
                updateName,
                updateScore,
                requiredAllPlayerName
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}