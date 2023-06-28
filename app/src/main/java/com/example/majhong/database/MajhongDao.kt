package com.example.majhong.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MajhongDao {
    @Upsert
    fun upsertMajhong(majhong: Majhong)

    @Query("SELECT * FROM majhong WHERE id=0")
    fun getMajhongById(): Majhong?
}