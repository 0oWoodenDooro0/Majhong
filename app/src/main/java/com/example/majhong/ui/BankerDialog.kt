package com.example.majhong.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.window.Dialog
import com.example.majhong.database.Player

@Composable
fun BankerDialog(
    onDismiss: () -> Unit,
    getPlayerByDirection: (Int) -> Player,
    resetBanker: (Int, Boolean, Boolean) -> Unit
) {
    var radioButtonSelected by remember { mutableStateOf(0) }
    var switchOfResetContinueToBank by remember { mutableStateOf(true) }
    var switchOfResetRoundWind by remember { mutableStateOf(false) }
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
                    .width(250.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "莊家設定",
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
                BankerRadioButton(
                    selected = { radioButtonSelected == 0 },
                    onClick = { radioButtonSelected = 0 },
                    text = getPlayerByDirection(0).name
                )
                BankerRadioButton(
                    selected = { radioButtonSelected == 1 },
                    onClick = { radioButtonSelected = 1 },
                    text = getPlayerByDirection(1).name
                )
                BankerRadioButton(
                    selected = { radioButtonSelected == 2 },
                    onClick = { radioButtonSelected = 2 },
                    text = getPlayerByDirection(2).name
                )
                BankerRadioButton(
                    selected = { radioButtonSelected == 3 },
                    onClick = { radioButtonSelected = 3 },
                    text = getPlayerByDirection(3).name
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "重製連莊數", fontSize = 20.sp, modifier = Modifier.padding(5.dp))
                    Switch(
                        checked = switchOfResetContinueToBank,
                        onCheckedChange = {
                            switchOfResetContinueToBank = !switchOfResetContinueToBank
                        }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "重製圈風計算", fontSize = 20.sp, modifier = Modifier.padding(5.dp))
                    Switch(
                        checked = switchOfResetRoundWind,
                        onCheckedChange = { switchOfResetRoundWind = !switchOfResetRoundWind }
                    )
                }
                TextButton(onClick = {
                    resetBanker(
                        radioButtonSelected,
                        switchOfResetContinueToBank,
                        switchOfResetRoundWind
                    )
                }, modifier = Modifier.padding(10.dp)) {
                    Text(text = "確定")
                }
            }
        }
    }
}

@Composable
fun BankerRadioButton(selected: () -> Boolean, onClick: () -> Unit, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        RadioButton(selected = selected(), onClick = onClick)
        Text(text = text, fontSize = 20.sp, modifier = Modifier.fillMaxWidth())
    }
}