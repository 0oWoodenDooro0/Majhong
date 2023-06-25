package com.example.majhong

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.majhong.ui.theme.MainColor
import com.example.majhong.ui.theme.MajhongTheme
import com.example.majhong.ui.theme.NegScoreColor
import com.example.majhong.ui.theme.PosScoreColor

class MainActivity : ComponentActivity() {

    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MajhongTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.background(MainColor),
                        verticalArrangement = Arrangement.Center
                    ) {
                        MainScreen(playerViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(playerViewModel: PlayerViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Top
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            Column(modifier = Modifier.weight(1f)) {
                PlayerCard(playerViewModel.players[2], playerViewModel)
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                PlayerCard(playerViewModel.players[3], playerViewModel)
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(modifier = Modifier.weight(1f)) {
                PlayerCard(playerViewModel.players[1], playerViewModel)
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            Column(modifier = Modifier.weight(1f)) {
                PlayerCard(playerViewModel.players[0], playerViewModel)
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerCard(player: Player, playerViewModel: PlayerViewModel) {
    val context = LocalContext.current
    val showWinDialog = remember { mutableStateOf(false) }
    val showAddNameDialog = remember { mutableStateOf(false) }
    val scoreColor =
        if (player.score == 0) Color.Black else if (player.score > 0) PosScoreColor else NegScoreColor
    if (showWinDialog.value) {
        WinDialog(
            onDismiss = {
                showWinDialog.value = false
            },
            bankerPlayer = playerViewModel.getBanker(),
            continueToBank = playerViewModel.continueToBank.value,
            currentPlayer = player,
            selectedPlayer = { index ->
                playerViewModel.players[index]
            },
            baseTai = playerViewModel.baseTai.value,
            tai = playerViewModel.tai.value,
            calculateTotal = { currentPlayer, selectedPlayer, numberOfTai ->
                playerViewModel.calculateTotal(currentPlayer, selectedPlayer, numberOfTai)
            }
        )
    }
    if (showAddNameDialog.value) {
        AddNameDialog(onDismiss = {
            showAddNameDialog.value = false
        }, buttonOnClick = { playerName ->
            playerViewModel.updatePlayerName(player, playerName)
            showAddNameDialog.value = false
        })
    }
    Card(shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(5.dp),
        onClick = {
            if (player.name == "") {
                showAddNameDialog.value = true
            } else if (!playerViewModel.getAllPlayerNamed()) {
                Toast.makeText(context, "請先加入所有玩家", Toast.LENGTH_LONG).show()
            } else {
                showWinDialog.value = true
            }
        }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (player.name != "") {
                Text(
                    text = player.name,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(5.dp),
                    color = Color.Gray
                )
                Text(
                    text = player.score.toString(),
                    fontSize = 36.sp,
                    modifier = Modifier.padding(5.dp),
                    color = scoreColor
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_person_add_alt_1_24),
                    contentDescription = stringResource(id = R.string.person_add_content)
                )
            }
        }
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WinDialog(
    onDismiss: () -> Unit,
    bankerPlayer: Player,
    continueToBank: Int,
    currentPlayer: Player,
    selectedPlayer: (Int) -> Player,
    baseTai: Int,
    tai: Int,
    calculateTotal: (Player, Player, Int) -> Int
) {
    AlertDialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.wrapContentSize(),
            shadowElevation = 5.dp
        ) {
            val stateOfTai = remember { mutableStateOf(0) }
            val stateOfPlayer = remember { mutableStateOf(0) }
            val selectedPlayerData = remember {
                mutableStateOf(selectedPlayer(stateOfPlayer.value))
            }
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "胡牌!", fontSize = 20.sp)
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "牌型台數",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 15.dp)
                        )
                        Button(onClick = {
                            if (stateOfTai.value < 50) stateOfTai.value += 1
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                                contentDescription = stringResource(id = R.string.arrow_up_content)
                            )
                        }
                        Text(
                            text = stateOfTai.value.toString(),
                            fontSize = 20.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                        Button(onClick = {
                            if (stateOfTai.value > 0) stateOfTai.value -= 1
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                                contentDescription = stringResource(id = R.string.arrow_down_content)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(40.dp))
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "放槍者",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 15.dp)
                        )
                        Button(onClick = {
                            if (stateOfPlayer.value < 3) stateOfPlayer.value += 1
                            selectedPlayerData.value = selectedPlayer(stateOfPlayer.value)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
                                contentDescription = stringResource(id = R.string.arrow_up_content)
                            )
                        }
                        Text(
                            text = if (selectedPlayerData.value == currentPlayer) "自摸" else selectedPlayerData.value.name,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                        Button(onClick = {
                            if (stateOfPlayer.value > 0) stateOfPlayer.value -= 1
                            selectedPlayerData.value = selectedPlayer(stateOfPlayer.value)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                                contentDescription = stringResource(id = R.string.arrow_down_content)
                            )
                        }
                    }
                }
                Divider(
                    modifier = Modifier.fillMaxWidth(), thickness = 2.dp, color = Color.LightGray
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "底", fontSize = 12.sp, color = Color.Gray)
                        Text(text = baseTai.toString(), fontSize = 12.sp)
                    }
                    Column(
                        modifier = Modifier.padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "牌型${stateOfTai.value}台",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(text = (tai * stateOfTai.value).toString(), fontSize = 12.sp)
                    }
                    if (currentPlayer == bankerPlayer || selectedPlayerData.value == bankerPlayer) {
                        Column(
                            modifier = Modifier.padding(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "莊${2 * continueToBank + 1}台",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = (tai * (2 * continueToBank + 1)).toString(),
                                fontSize = 12.sp
                            )
                        }
                    }
                    if (currentPlayer == selectedPlayerData.value) {
                        Column(
                            modifier = Modifier.padding(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "自摸", fontSize = 12.sp, color = Color.Gray)
                            Text(text = "x3", fontSize = 12.sp)
                        }
                    }
                    if (currentPlayer == selectedPlayerData.value && currentPlayer != bankerPlayer) {
                        Column(
                            modifier = Modifier.padding(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "莊${2 * continueToBank + 1}台",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = (tai * (2 * continueToBank + 1)).toString(),
                                fontSize = 12.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "總數",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = calculateTotal(
                                currentPlayer,
                                selectedPlayerData.value,
                                stateOfTai.value
                            ).toString(),
                            fontSize = 12.sp
                        )
                    }
                }
                Button(onClick = {}) {
                    Text(text = "確定")
                }
            }
        }
    }
}