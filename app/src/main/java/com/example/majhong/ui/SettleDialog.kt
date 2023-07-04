package com.example.majhong.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.majhong.database.Player

@Composable
fun SettleDialog(
    onDismiss: () -> Unit, players: () -> List<Player>
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(15.dp),
            shadowElevation = 5.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .width(200.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "結算", modifier = Modifier.padding(10.dp), fontSize = 24.sp)
                LazyColumn(modifier = Modifier.padding(5.dp)) {
                    items(items = players(), key = { item -> item.id }) {
                        PlayerSettle(player = it)
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerSettle(player: Player) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = player.name, fontSize = 20.sp)
        Text(text = player.score.toString(), fontSize = 20.sp)
    }
}