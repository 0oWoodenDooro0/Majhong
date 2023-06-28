package com.example.majhong.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.majhong.R

@Composable
fun DiceDialog(
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Image(
            painter = painterResource(id = R.drawable.dice1),
            contentDescription = stringResource(id = R.string.dice_content)
        )
    }
}