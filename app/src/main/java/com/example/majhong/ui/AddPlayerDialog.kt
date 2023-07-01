package com.example.majhong.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun AddPlayerDialog(onDismiss: () -> Unit, buttonOnClick: (String) -> Unit) {
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
                    onValueChange = { if (it.length <= maxChar) name = it },
                    label = { Text("玩家名稱") },
                    singleLine = true,
                    supportingText = {
                        Text(
                            text = "${name.length} / $maxChar",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                        )
                    })
                TextButton(onClick = { buttonOnClick(name) }) {
                    Text(text = "確定")
                }
            }
        }
    }
}
