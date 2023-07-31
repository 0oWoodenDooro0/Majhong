package com.example.majhong.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MajhongHistoryDao {
    @Upsert
    suspend fun upsertMajhongHistory(majhongHistory: MajhongHistory)

    @Query("DELETE FROM majhonghistory WHERE id=:id")
    suspend fun deleteMajhongHistoryById(id: Int)

    @Query("DELETE FROM majhonghistory")
    suspend fun deleteAllMajhongHistory()

    @Query("SELECT * FROM majhonghistory WHERE id=:id")
    suspend fun findMajhongHistoryById(id: Int): MajhongHistory?
}