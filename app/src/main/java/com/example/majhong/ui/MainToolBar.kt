package com.example.majhong.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.majhong.R
import com.example.majhong.database.Player

@Composable
fun MainToolBar(
    baseTai: () -> Int,
    tai: () -> Int,
    drawToContinue: () -> Boolean,
    newToClearPlayer: () -> Boolean,
    onModifyRules: (Int, Int, Boolean, Boolean) -> Unit,
    AddPlayer: (String) -> Unit,
    players: () -> List<Player>,
    swapPlayer: (Player, Player) -> Unit,
    isNameRepeated: (String) -> Boolean
) {
    var showNewDialog by remember { mutableStateOf(false) }
    var showModifyRulesDialog by remember { mutableStateOf(false) }
    var showAddPlayerDialog by remember { mutableStateOf(false) }
    var showTranspositionDialog by remember { mutableStateOf(false) }
    var showDiceDialog by remember { mutableStateOf(false) }
    Row(modifier = Modifier.fillMaxWidth()) {
        ActionButton(modifier = Modifier
            .weight(1f)
            .clickable { showNewDialog = true }
            .padding(10.dp),
            painterResourceId = R.drawable.baseline_add_24,
            stringResource = R.string.add_content,
            actionDescription = "新牌局")
        ActionButton(modifier = Modifier
            .weight(1f)
            .clickable { }
            .padding(10.dp),
            painterResourceId = R.drawable.baseline_undo_24,
            stringResource = R.string.undo_content,
            actionDescription = "還原")
        ActionButton(modifier = Modifier
            .weight(1f)
            .clickable { showAddPlayerDialog = true }
            .padding(10.dp),
            painterResourceId = R.drawable.baseline_person_add_alt_1_24,
            stringResource = R.string.person_add_content,
            actionDescription = "新增玩家")
        ActionButton(modifier = Modifier
            .weight(1f)
            .clickable { showTranspositionDialog = true }
            .padding(10.dp),
            painterResourceId = R.drawable.baseline_swap_vert_24,
            stringResource = R.string.swap_content,
            actionDescription = "換人/換位")
        ActionButton(modifier = Modifier
            .weight(1f)
            .clickable { showDiceDialog = true }
            .padding(10.dp),
            painterResourceId = R.drawable.outline_casino_24,
            stringResource = R.string.casino_content,
            actionDescription = "擲骰")
    }
    if (showNewDialog) {
        NewDialog(onDismiss = { showNewDialog = false }, onConfirm = {
            showNewDialog = false
            showModifyRulesDialog = true
        })
    } else if (showModifyRulesDialog) {
        ModifyRulesDialog(baseTai = baseTai,
            tai = tai,
            drawToContinue = drawToContinue,
            newToClearPlayer = newToClearPlayer,
            onDismiss = { showModifyRulesDialog = false },
            onModifyRules = { baseTaiValue, taiValue, switchOfDraw, switchOfClearPlayer ->
                onModifyRules(baseTaiValue, taiValue, switchOfDraw, switchOfClearPlayer)
                showModifyRulesDialog = false
            })
    } else if (showAddPlayerDialog) {
        AddPlayerDialog(
            onDismiss = { showAddPlayerDialog = false },
            isNameRepeated = isNameRepeated,
            buttonOnClick = { name ->
                AddPlayer(name)
                showAddPlayerDialog = false
            }
        )
    } else if (showTranspositionDialog) {
        TranspositionDialog(onDismiss = { showTranspositionDialog = false },
            players = players,
            swapPlayers = { player1, player2 ->
                swapPlayer(player1, player2)
                showTranspositionDialog = false
            })
    } else if (showDiceDialog) {
        DiceDialog {
            showDiceDialog = false
        }
    }
}

@Composable
fun ActionButton(
    modifier: Modifier, painterResourceId: Int, stringResource: Int, actionDescription: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(painterResourceId),
            contentDescription = stringResource(stringResource),
            modifier = Modifier.padding(5.dp)
        )
        Text(
            text = actionDescription, fontSize = 12.sp
        )
    }
}