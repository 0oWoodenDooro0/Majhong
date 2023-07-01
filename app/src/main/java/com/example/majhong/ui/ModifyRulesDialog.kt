package com.example.majhong.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import com.example.majhong.R

@Composable
fun ModifyRulesDialog(
    baseTai: () -> Int,
    tai: () -> Int,
    drawToContinue: () -> Boolean,
    newToClearPlayer: () -> Boolean,
    onDismiss: () -> Unit,
    onModifyRules: (Int, Int, Boolean, Boolean) -> Unit
) {
    var baseTaiText by remember { mutableStateOf(baseTai().toString()) }
    var baseTaiError by remember { mutableStateOf(false) }
    var taiText by remember { mutableStateOf(tai().toString()) }
    var taiError by remember { mutableStateOf(false) }
    var switchOfDraw by remember { mutableStateOf(drawToContinue()) }
    var switchOfClearPlayer by remember { mutableStateOf(newToClearPlayer()) }
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(15.dp),
            shadowElevation = 5.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "規則細項", modifier = Modifier.padding(10.dp), fontSize = 24.sp)
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "底台", fontSize = 20.sp)
                    OutlinedTextField(
                        value = baseTaiText,
                        onValueChange = {
                            baseTaiText = it
                            baseTaiError = !it.isDigitsOnly()
                        },
                        label = { Text("底台") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(150.dp),
                        isError = baseTaiError,
                        supportingText = {
                            if (baseTaiError) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "請輸入正確數字",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        trailingIcon = {
                            if (baseTaiError) Icon(
                                painter = painterResource(id = R.drawable.baseline_error_24),
                                contentDescription = stringResource(id = R.string.error_content),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "台數", fontSize = 20.sp)
                    OutlinedTextField(
                        value = taiText,
                        onValueChange = {
                            taiText = it
                            taiError = !it.isDigitsOnly()
                        },
                        label = { Text("台數") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(150.dp),
                        isError = taiError,
                        supportingText = {
                            if (taiError) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "請輸入正確數字",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        trailingIcon = {
                            if (taiError) Icon(
                                painter = painterResource(id = R.drawable.baseline_error_24),
                                contentDescription = stringResource(id = R.string.error_content),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    thickness = 2.dp
                )
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "流局是否連莊", fontSize = 20.sp)
                    Switch(checked = switchOfDraw, onCheckedChange = {
                        switchOfDraw = it
                    })
                }
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "是否清除玩家", fontSize = 20.sp)
                    Switch(checked = switchOfClearPlayer, onCheckedChange = {
                        switchOfClearPlayer = it
                    })
                }
                TextButton(onClick = {
                    if (baseTaiText.isDigitsOnly())
                        if (baseTaiText.isDigitsOnly() && taiText.isDigitsOnly()) onModifyRules(
                            baseTaiText.toInt(),
                            taiText.toInt(),
                            switchOfDraw,
                            switchOfClearPlayer
                        )
                }, modifier = Modifier.padding(10.dp)) {
                    Text(text = "確定")
                }
            }
        }
    }
}
