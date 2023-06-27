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
import com.example.majhong.ui.theme.BankerColor
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
                        modifier = Modifier.background(MainColor)
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
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val players = playerViewModel.players
        val bankerPlayer = { playerViewModel.banker.value }
        val continueToBank = { playerViewModel.continueToBank.value }
        val selectedPlayer: (Int) -> Player = { index ->
            playerViewModel.players[index]
        }
        val baseTai = { playerViewModel.baseTai.value }
        val tai = { playerViewModel.tai.value }
        val calculateTotal: (Player, Player, Int) -> Int = { current, selected, numberOfTai ->
            playerViewModel.calculateTotal(current, selected, numberOfTai)
        }
        val updateName: (Player, String) -> Unit = { current, playerName ->
            playerViewModel.updatePlayerName(current, playerName)
        }
        val updateScore: (Player, Player, Int) -> Unit = { current, selected, numberOfTai ->
            playerViewModel.updateScore(current, selected, numberOfTai)
        }
        Row(modifier = Modifier.padding(15.dp)) {
            Text(
                text = "${playerViewModel.round.value}圈${playerViewModel.wind.value}風",
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    .padding(10.dp),
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Column(modifier = Modifier.weight(1f)) {
                PlayerCard(
                    playerViewModel,
                    players[2],
                    bankerPlayer,
                    continueToBank,
                    selectedPlayer,
                    baseTai,
                    tai,
                    calculateTotal,
                    updateName,
                    updateScore
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                PlayerCard(
                    playerViewModel,
                    players[3],
                    bankerPlayer,
                    continueToBank,
                    selectedPlayer,
                    baseTai,
                    tai,
                    calculateTotal,
                    updateName,
                    updateScore
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = { playerViewModel.draw() }) {
                    Text(text = "流局")
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                PlayerCard(
                    playerViewModel,
                    players[1],
                    bankerPlayer,
                    continueToBank,
                    selectedPlayer,
                    baseTai,
                    tai,
                    calculateTotal,
                    updateName,
                    updateScore
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Column(modifier = Modifier.weight(1f)) {
                PlayerCard(
                    playerViewModel,
                    players[0],
                    bankerPlayer,
                    continueToBank,
                    selectedPlayer,
                    baseTai,
                    tai,
                    calculateTotal,
                    updateName,
                    updateScore
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerCard(
    playerViewModel: PlayerViewModel,
    currentPlayer: Player,
    bankerPlayer: () -> Player,
    continueToBank: () -> Int,
    selectedPlayer: (Int) -> Player,
    baseTai: () -> Int,
    tai: () -> Int,
    calculateTotal: (Player, Player, Int) -> Int,
    updateName: (Player, String) -> Unit,
    updateScore: (Player, Player, Int) -> Unit
) {
    val context = LocalContext.current
    val showWinDialog = remember { mutableStateOf(false) }
    val showAddNameDialog = remember { mutableStateOf(false) }
    val scoreColor =
        if (currentPlayer.score.value == 0) Color.Black else if (currentPlayer.score.value > 0) PosScoreColor else NegScoreColor
    if (showWinDialog.value) {
        WinDialog(onDismiss = {
            showWinDialog.value = false
        },
            bankerPlayer = bankerPlayer,
            continueToBank = continueToBank,
            currentPlayer = currentPlayer,
            selectedPlayer = selectedPlayer,
            baseTai = baseTai,
            tai = tai,
            calculateTotal = { selected, numberOfTai ->
                calculateTotal(currentPlayer, selected, numberOfTai)
            },
            buttonOnClick = { selected, numberOfTai ->
                updateScore(currentPlayer, selected, numberOfTai)
                showWinDialog.value = false
            })
    }
    if (showAddNameDialog.value) {
        AddNameDialog(onDismiss = {
            showAddNameDialog.value = false
        }, buttonOnClick = { playerName ->
            updateName(currentPlayer, playerName)
            showAddNameDialog.value = false
        })
    }
    Card(shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(5.dp),
        onClick = {
            if (currentPlayer.name == "") {
                showAddNameDialog.value = true
            } else if (!playerViewModel.isAllPlayerNamed()) {
                Toast.makeText(context, "請先加入所有玩家", Toast.LENGTH_LONG).show()
            } else {
                showWinDialog.value = true
            }
        }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentPlayer.name != "") {
                Text(
                    text = currentPlayer.name,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(5.dp),
                    color = Color.Gray
                )
                Text(
                    text = currentPlayer.score.value.toString(),
                    modifier = Modifier.padding(10.dp),
                    fontSize = 32.sp,
                    color = scoreColor
                )
                if (currentPlayer == bankerPlayer() && continueToBank() == 0) {
                    Text(
                        text = "莊",
                        modifier = Modifier
                            .background(
                                color = BankerColor, shape = RoundedCornerShape(5.dp)
                            )
                            .padding(5.dp),
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                    )
                } else if (currentPlayer == bankerPlayer()) {
                    Text(
                        text = "連${continueToBank()}",
                        modifier = Modifier
                            .background(
                                color = BankerColor, shape = RoundedCornerShape(5.dp)
                            )
                            .padding(5.dp),
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                    )
                }
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
    bankerPlayer: () -> Player,
    continueToBank: () -> Int,
    currentPlayer: Player,
    selectedPlayer: (Int) -> Player,
    baseTai: () -> Int,
    tai: () -> Int,
    calculateTotal: (Player, Int) -> Int,
    buttonOnClick: (Player, Int) -> Unit
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
                        Text(text = baseTai().toString(), fontSize = 12.sp)
                    }
                    Column(
                        modifier = Modifier.padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "牌型${stateOfTai.value}台", fontSize = 12.sp, color = Color.Gray
                        )
                        Text(text = (tai() * stateOfTai.value).toString(), fontSize = 12.sp)
                    }
                    if (currentPlayer == bankerPlayer() || selectedPlayerData.value == bankerPlayer()) {
                        Column(
                            modifier = Modifier.padding(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "莊${2 * continueToBank() + 1}台",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = (tai() * (2 * continueToBank() + 1)).toString(),
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
                    if (currentPlayer == selectedPlayerData.value && currentPlayer != bankerPlayer()) {
                        Column(
                            modifier = Modifier.padding(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "莊${2 * continueToBank() + 1}台",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = (tai() * (2 * continueToBank() + 1)).toString(),
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
                            text = "總數", fontSize = 12.sp, color = Color.Gray
                        )
                        Text(
                            text = calculateTotal(
                                selectedPlayerData.value, stateOfTai.value
                            ).toString(), fontSize = 12.sp
                        )
                    }
                }
                Button(onClick = {
                    buttonOnClick(
                        selectedPlayerData.value, stateOfTai.value
                    )
                }) {
                    Text(text = "確定")
                }
            }
        }
    }
}