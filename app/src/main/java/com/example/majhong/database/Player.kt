package com.example.majhong.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    var name: String = "",
    var score: Int = 0,
    var direction: Int = -1,
    var winCount: Int = 0,
    var selfDrawnCount: Int = 0,
    var chunkCount: Int = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)