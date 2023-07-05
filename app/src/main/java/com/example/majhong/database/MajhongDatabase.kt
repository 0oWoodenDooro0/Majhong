package com.example.majhong.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Player::class, Majhong::class, MajhongHistory::class],
    version = 3
)
abstract class MajhongDatabase : RoomDatabase() {
    abstract val playerDao: PlayerDao
    abstract val majhongDao: MajhongDao
    abstract val majhongHistoryDao: MajhongHistoryDao
}