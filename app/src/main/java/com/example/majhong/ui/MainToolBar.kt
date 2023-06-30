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

@Composable
fun MainToolBar() {
    var showDiceDialog by remember { mutableStateOf(false) }
    Row(modifier = Modifier.fillMaxWidth()) {
        ActionButton(
            modifier = Modifier
                .weight(1f)
                .clickable { }
                .padding(10.dp),
            painterResourceId = R.drawable.baseline_add_24,
            stringResource = R.string.add_content,
            actionDescription = "新牌局"
        )
        ActionButton(
            modifier = Modifier
                .weight(1f)
                .clickable { }
                .padding(10.dp),
            painterResourceId = R.drawable.baseline_undo_24,
            stringResource = R.string.undo_content,
            actionDescription = "還原"
        )
        ActionButton(
            modifier = Modifier
                .weight(1f)
                .clickable { }
                .padding(10.dp),
            painterResourceId = R.drawable.baseline_person_add_alt_1_24,
            stringResource = R.string.person_add_content,
            actionDescription = "新增玩家"
        )
        ActionButton(
            modifier = Modifier
                .weight(1f)
                .clickable { }
                .padding(10.dp),
            painterResourceId = R.drawable.baseline_swap_vert_24,
            stringResource = R.string.swap_vert_content,
            actionDescription = "換人/換位"
        )
        ActionButton(
            modifier = Modifier
                .weight(1f)
                .clickable { showDiceDialog = true}
                .padding(10.dp),
            painterResourceId = R.drawable.outline_casino_24,
            stringResource = R.string.casino_content,
            actionDescription = "擲骰"
        )
    }
    if (showDiceDialog){
        DiceDialog {
            showDiceDialog = false
        }
    }
}

@Composable
fun ActionButton(
    modifier: Modifier,
    painterResourceId: Int,
    stringResource: Int,
    actionDescription: String
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
            text = actionDescription,
            fontSize = 12.sp
        )
    }
}