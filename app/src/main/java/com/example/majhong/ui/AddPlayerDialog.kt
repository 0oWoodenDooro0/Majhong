package com.example.majhong.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.majhong.R

@Composable
fun AddPlayerDialog(
    onDismiss: () -> Unit,
    isNameRepeated: (String) -> Boolean,
    buttonOnClick: (String) -> Unit
) {
    var nameError by remember { mutableStateOf(false) }
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(15.dp),
            shadowElevation = 5.dp
        ) {
            var name by remember { mutableStateOf("") }
            val maxChar = 5
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "輸入玩家名稱", fontSize = 20.sp, modifier = Modifier.padding(10.dp)
                )
                TextField(value = name,
                    onValueChange = {
                        if (it.length <= maxChar) name = it
                        nameError = isNameRepeated(name)
                    },
                    label = { Text("玩家名稱") },
                    singleLine = true,
                    isError = nameError,
                    supportingText = {
                        Row {
                            if (nameError) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = "名字不可重複",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            Text(
                                text = "${name.length} / $maxChar",
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End,
                            )
                        }
                    },
                    trailingIcon = {
                        if (nameError) Icon(
                            painter = painterResource(id = R.drawable.baseline_error_24),
                            contentDescription = stringResource(id = R.string.error_content),
                            tint = MaterialTheme.colorScheme.error
                        )
                    })
                TextButton(onClick = { if (!nameError) buttonOnClick(name) }) {
                    Text(text = "確定")
                }
            }
        }
    }
}
