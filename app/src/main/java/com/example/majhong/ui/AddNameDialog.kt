package com.example.majhong.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNameDialog(onDismiss: () -> Unit, buttonOnClick: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.wrapContentSize(),
            shadowElevation = 5.dp
        ) {
            val name = remember { mutableStateOf("") }
            val maxChar = 5
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "輸入玩家名稱", fontSize = 20.sp, modifier = Modifier.padding(10.dp)
                )
                TextField(value = name.value,
                    onValueChange = { if (it.length <= maxChar) name.value = it },
                    label = { Text("玩家名稱") },
                    singleLine = true,
                    supportingText = {
                        Text(
                            text = "${name.value.length} / $maxChar",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                        )
                    })
                Button(onClick = { buttonOnClick(name.value) }) {
                    Text(text = "確定")
                }
            }
        }
    }
}
