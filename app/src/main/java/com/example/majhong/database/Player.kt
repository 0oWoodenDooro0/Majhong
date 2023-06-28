package com.example.majhong.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    var name: String,
    var score: Int,
    var direction: Int = -1,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)