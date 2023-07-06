package com.example.majhong.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.majhong.database.Player

@Composable
fun CountScreen(playerList: () -> List<Player>) {
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
        items(playerList()) { player ->
            CountCard(
                playerName = player.name,
                winCount = player.winCount,
                selfDrawnCount = player.selfDrawnCount,
                chunkCount = player.chunkCount
            )
        }
    }
}

@Composable
fun CountCard(playerName: String, winCount: Int, selfDrawnCount: Int, chunkCount: Int) {
    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.padding(5.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column {
            Text(
                text = playerName,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .padding(start = 10.dp)
            )
            Row {
                CountItem(modifier = Modifier.weight(1f), count = winCount, text = "胡牌")
                CountItem(modifier = Modifier.weight(1f), count = selfDrawnCount, text = "自摸")
                CountItem(modifier = Modifier.weight(1f), count = chunkCount, text = "放槍")
            }
        }
    }
}

@Composable
fun CountItem(modifier: Modifier, count: Int, text: String) {
    Column(modifier = modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count.toString(), fontSize = 32.sp)
        Text(text = text, fontSize = 16.sp)
    }
}