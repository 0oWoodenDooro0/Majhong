package com.example.majhong.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.majhong.MajhongViewModel
import com.example.majhong.PlayerState

@Composable
fun MainScreen(viewModel: MajhongViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val players = viewModel.playerStates
        val bankerPlayer = { viewModel.getBanker() }
        val continueToBank = { viewModel.continueToBank }
        val selectedPlayerState: (Int) -> PlayerState = { index ->
            viewModel.playerStates[index]
        }
        val baseTai = { viewModel.baseTai }
        val tai = { viewModel.tai }
        val isAllPlayerNamed = { viewModel.isAllPlayerNamed() }
        val calculateTotal: (PlayerState, PlayerState, Int) -> Int =
            { current, selected, numberOfTai ->
                viewModel.calculateTotal(current, selected, numberOfTai)
            }
        val updateName: (PlayerState, String) -> Unit = { current, playerName ->
            viewModel.updatePlayerName(current, playerName)
        }
        val updateScore: (PlayerState, PlayerState, Int) -> Unit =
            { current, selected, numberOfTai ->
                viewModel.updateScore(current, selected, numberOfTai)
            }
        Row(modifier = Modifier.padding(15.dp)) {
            Text(
                text = "${viewModel.directions[viewModel.round]}圈${viewModel.directions[viewModel.wind]}風",
                modifier = Modifier
                    .background(
                        color = Color.White, shape = RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp),
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
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
                players[2],
                bankerPlayer,
                continueToBank,
                selectedPlayerState,
                baseTai,
                tai,
                isAllPlayerNamed,
                calculateTotal,
                updateName,
                updateScore
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
                bankerPlayer,
                continueToBank,
                selectedPlayerState,
                baseTai,
                tai,
                isAllPlayerNamed,
                calculateTotal,
                updateName,
                updateScore
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = { viewModel.draw() }) {
                    Text(text = "流局")
                }
            }
            PlayerCard(
                Modifier.weight(1f),
                players[1],
                bankerPlayer,
                continueToBank,
                selectedPlayerState,
                baseTai,
                tai,
                isAllPlayerNamed,
                calculateTotal,
                updateName,
                updateScore
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
                bankerPlayer,
                continueToBank,
                selectedPlayerState,
                baseTai,
                tai,
                isAllPlayerNamed,
                calculateTotal,
                updateName,
                updateScore
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}