package com.example.majhong.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NewDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "確定")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "取消")
            }
        },
        title = { Text(text = "開新牌局？") },
        text = { Text(text = "之前的牌局紀錄將會被清除") }
    )
}