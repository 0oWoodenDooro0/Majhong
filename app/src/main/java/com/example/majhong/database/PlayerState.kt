package com.example.majhong.database

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class PlayerState(
    var name: String = "", var score: MutableState<Int> = mutableStateOf(0), var id: Int = 0
)