package com.example.majhong.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.majhong.R

@Composable
fun DiceDialog(
    onDismissRequest: () -> Unit
) {
    val diceList = listOf(
        R.drawable.dice_01,
        R.drawable.dice_02,
        R.drawable.dice_03,
        R.drawable.dice_04,
        R.drawable.dice_05,
        R.drawable.dice_06
    )
    var diceResult by remember { mutableStateOf(0) }
    var dice1 by remember { mutableStateOf(0) }
    var dice2 by remember { mutableStateOf(0) }
    var dice3 by remember { mutableStateOf(0) }
    val diceScoreList = listOf(dice1, dice2, dice3)
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(15.dp),
            shadowElevation = 5.dp
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(200.dp)
            ) {
                Text(
                    text = diceResult.toString(),
                    fontSize = 30.sp,
                    modifier = Modifier.padding(10.dp)
                )
                LazyRow(modifier = Modifier.padding(10.dp)) {
                    items(3) {
                        Image(
                            painter = painterResource(id = diceList[diceScoreList[it]]),
                            contentDescription = stringResource(id = R.string.dice_content),
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
                Button(onClick = {
                    dice1 = getRandomDice()
                    dice2 = getRandomDice()
                    dice3 = getRandomDice()
                    diceResult = dice1 + dice2 + dice3 + 3
                }) {
                    Text(text = "擲骰")
                }
            }
        }
    }
}

fun getRandomDice(): Int {
    return (0 until 6).random()
}

@Preview
@Composable
fun TestDice() {
    MaterialTheme {
        DiceDialog {
        }
    }
}