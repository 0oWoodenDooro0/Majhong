package com.example.majhong.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.majhong.R
import com.example.majhong.database.Player

@Composable
fun TranspositionDialog(
    onDismiss: () -> Unit,
    players: List<Player>,
    swapPlayers: (Player, Player) -> Unit
) {
    val playerList by remember { mutableStateOf(players) }
    var stateOfLeftIndex by remember { mutableStateOf(0) }
    var stateOfRightIndex by remember { mutableStateOf(1) }
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(15.dp),
            shadowElevation = 5.dp
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "規則細項", modifier = Modifier.padding(10.dp), fontSize = 24.sp)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextButton(onClick = {
                            if (stateOfLeftIndex + 1 != stateOfRightIndex && stateOfLeftIndex + 1 < playerList.size) stateOfLeftIndex++
                            else if (stateOfLeftIndex + 2 < playerList.size) stateOfLeftIndex += 2
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                                contentDescription = stringResource(id = R.string.arrow_up_content)
                            )
                        }
                        Text(
                            text = playerList[stateOfLeftIndex].name,
                            fontSize = 20.sp
                        )
                        TextButton(onClick = {
                            if (stateOfLeftIndex - 1 != stateOfRightIndex && stateOfLeftIndex - 1 >= 0) stateOfLeftIndex--
                            else if (stateOfLeftIndex - 2 >= 0) stateOfLeftIndex -= 2
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                                contentDescription = stringResource(id = R.string.arrow_down_content)
                            )
                        }
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_swap_horiz_24),
                        contentDescription = stringResource(id = R.string.swap_content)
                    )
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextButton(onClick = {
                            if (stateOfRightIndex + 1 != stateOfLeftIndex && stateOfRightIndex + 1 < playerList.size) stateOfRightIndex++
                            else if (stateOfRightIndex + 2 < playerList.size) stateOfRightIndex += 2
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                                contentDescription = stringResource(id = R.string.arrow_up_content)
                            )
                        }
                        Text(
                            text = playerList[stateOfRightIndex].name,
                            fontSize = 20.sp
                        )
                        TextButton(onClick = {
                            if (stateOfRightIndex - 1 != stateOfLeftIndex && stateOfRightIndex - 1 > 0) stateOfRightIndex--
                            else if (stateOfRightIndex - 2 > 0) stateOfRightIndex -= 2
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                                contentDescription = stringResource(id = R.string.arrow_down_content)
                            )
                        }
                    }
                }
                TextButton(onClick = {
                    swapPlayers(
                        playerList[stateOfLeftIndex],
                        playerList[stateOfRightIndex]
                    )
                }) {
                    Text(text = "確認")
                }
            }
        }
    }
}