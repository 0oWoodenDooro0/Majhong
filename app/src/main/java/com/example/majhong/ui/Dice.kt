package com.example.majhong.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.majhong.R

@Composable
fun DiceDialog(
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.dice_01),
                contentDescription = stringResource(id = R.string.dice_content),
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.dice_02),
                contentDescription = stringResource(id = R.string.dice_content),
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.dice_03),
                contentDescription = stringResource(id = R.string.dice_content),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview
@Composable
fun TestDice() {
    MaterialTheme {
        DiceDialog {
        }
    }
}