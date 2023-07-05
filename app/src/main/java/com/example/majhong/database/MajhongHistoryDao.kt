package com.example.majhong.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MajhongHistoryDao {
    @Upsert
    fun upsertMajhongHistory(majhongHistory: MajhongHistory)

    @Query("DELETE FROM majhonghistory WHERE id=:id")
    fun deleteMajhongHistoryById(id: Int)

    @Query("DELETE FROM majhonghistory")
    fun deleteAllMajhongHistory()
}