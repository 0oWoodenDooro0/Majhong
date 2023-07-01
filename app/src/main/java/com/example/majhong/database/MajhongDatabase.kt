package com.example.majhong.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Player::class, Majhong::class],
    version = 2
)
abstract class MajhongDatabase : RoomDatabase() {
    abstract val playerDao: PlayerDao
    abstract val majhongDao: MajhongDao
}