package com.example.majhong.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.majhong.R
import com.example.majhong.database.Player

@Composable
fun WinDialog(
    onDismiss: () -> Unit,
    currentPlayerIsBanker: (Player) -> Boolean,
    selectedPlayerIsBanker: (Player) -> Boolean,
    continueToBank: () -> Int,
    currentPlayer: Player,
    selectedPlayer: (Int) -> Player,
    baseTai: () -> Int,
    tai: () -> Int,
    calculateTotal: (Player, Int) -> Int,
    buttonOnClick: (Player, Int) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(15.dp),
            shadowElevation = 5.dp
        ) {
            var stateOfTai by remember { mutableStateOf(0) }
            var stateOfPlayer by remember { mutableStateOf(0) }
            var selectedPlayerData by remember { mutableStateOf(selectedPlayer(stateOfPlayer)) }
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "胡牌!", fontSize = 20.sp)
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "牌型台數",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 15.dp)
                        )
                        TextButton(onClick = {
                            if (stateOfTai < 50) stateOfTai++
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                                contentDescription = stringResource(id = R.string.arrow_up_content)
                            )
                        }
                        Text(
                            text = stateOfTai.toString(),
                            fontSize = 20.sp
                        )
                        TextButton(onClick = {
                            if (stateOfTai > 0) stateOfTai--
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                                contentDescription = stringResource(id = R.string.arrow_down_content)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(40.dp))
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "放槍者",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 15.dp)
                        )
                        TextButton(onClick = {
                            if (stateOfPlayer < 3) stateOfPlayer += 1
                            selectedPlayerData = selectedPlayer(stateOfPlayer)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                                contentDescription = stringResource(id = R.string.arrow_up_content)
                            )
                        }
                        AutoSizeText(
                            text = if (selectedPlayerData == currentPlayer) "自摸" else selectedPlayerData.name,
                            maxTextSize = 20.sp
                        )
                        TextButton(onClick = {
                            if (stateOfPlayer > 0) stateOfPlayer -= 1
                            selectedPlayerData = selectedPlayer(stateOfPlayer)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                                contentDescription = stringResource(id = R.string.arrow_down_content)
                            )
                        }
                    }
                }
                Divider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "底", fontSize = 12.sp, color = Color.Gray)
                        Text(text = baseTai().toString(), fontSize = 12.sp)
                    }
                    Column(
                        modifier = Modifier.padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "牌型${stateOfTai}台", fontSize = 12.sp, color = Color.Gray)
                        Text(text = (tai() * stateOfTai).toString(), fontSize = 12.sp)
                    }
                    if (currentPlayerIsBanker(currentPlayer) || selectedPlayerIsBanker(
                            selectedPlayerData
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "莊${2 * continueToBank() + 1}台",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = (tai() * (2 * continueToBank() + 1)).toString(),
                                fontSize = 12.sp
                            )
                        }
                    }
                    if (currentPlayer == selectedPlayerData) {
                        Column(
                            modifier = Modifier.padding(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "自摸", fontSize = 12.sp, color = Color.Gray)
                            Text(text = "x3", fontSize = 12.sp)
                        }
                    }
                    if (currentPlayer == selectedPlayerData && !currentPlayerIsBanker(
                            currentPlayer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "莊${2 * continueToBank() + 1}台",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = (tai() * (2 * continueToBank() + 1)).toString(),
                                fontSize = 12.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "總數", fontSize = 12.sp, color = Color.Gray
                        )
                        Text(
                            text = calculateTotal(selectedPlayerData, stateOfTai).toString(),
                            fontSize = 12.sp
                        )
                    }
                }
                TextButton(onClick = {
                    buttonOnClick(selectedPlayerData, stateOfTai)
                }) {
                    Text(text = "確定")
                }
            }
        }
    }
}