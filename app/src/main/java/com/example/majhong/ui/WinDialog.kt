package com.example.majhong.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.majhong.PlayerState
import com.example.majhong.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WinDialog(
    onDismiss: () -> Unit,
    bankerPlayerState: () -> PlayerState,
    continueToBank: () -> Int,
    currentPlayerState: PlayerState,
    selectedPlayerState: (Int) -> PlayerState,
    baseTai: () -> Int,
    tai: () -> Int,
    calculateTotal: (PlayerState, Int) -> Int,
    buttonOnClick: (PlayerState, Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.wrapContentSize(),
            shadowElevation = 5.dp
        ) {
            val stateOfTai = remember { mutableStateOf(0) }
            val stateOfPlayer = remember { mutableStateOf(0) }
            val selectedPlayerData = remember {
                mutableStateOf(selectedPlayerState(stateOfPlayer.value))
            }
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
                        Button(onClick = {
                            if (stateOfTai.value < 50) stateOfTai.value += 1
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                                contentDescription = stringResource(id = R.string.arrow_up_content)
                            )
                        }
                        Text(
                            text = stateOfTai.value.toString(),
                            fontSize = 20.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                        Button(onClick = {
                            if (stateOfTai.value > 0) stateOfTai.value -= 1
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
                        Button(onClick = {
                            if (stateOfPlayer.value < 3) stateOfPlayer.value += 1
                            selectedPlayerData.value = selectedPlayerState(stateOfPlayer.value)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                                contentDescription = stringResource(id = R.string.arrow_up_content)
                            )
                        }
                        Text(
                            text = if (selectedPlayerData.value == currentPlayerState) "自摸" else selectedPlayerData.value.name,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                        Button(onClick = {
                            if (stateOfPlayer.value > 0) stateOfPlayer.value -= 1
                            selectedPlayerData.value = selectedPlayerState(stateOfPlayer.value)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                                contentDescription = stringResource(id = R.string.arrow_down_content)
                            )
                        }
                    }
                }
                Divider(
                    modifier = Modifier.fillMaxWidth(), thickness = 2.dp, color = Color.LightGray
                )
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
                        Text(
                            text = "牌型${stateOfTai.value}台", fontSize = 12.sp, color = Color.Gray
                        )
                        Text(text = (tai() * stateOfTai.value).toString(), fontSize = 12.sp)
                    }
                    if (currentPlayerState == bankerPlayerState() || selectedPlayerData.value == bankerPlayerState()) {
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
                    if (currentPlayerState == selectedPlayerData.value) {
                        Column(
                            modifier = Modifier.padding(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "自摸", fontSize = 12.sp, color = Color.Gray)
                            Text(text = "x3", fontSize = 12.sp)
                        }
                    }
                    if (currentPlayerState == selectedPlayerData.value && currentPlayerState != bankerPlayerState()) {
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
                            text = calculateTotal(
                                selectedPlayerData.value, stateOfTai.value
                            ).toString(), fontSize = 12.sp
                        )
                    }
                }
                Button(onClick = {
                    buttonOnClick(
                        selectedPlayerData.value, stateOfTai.value
                    )
                }) {
                    Text(text = "確定")
                }
            }
        }
    }
}