package com.example.majhong.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PlayerDao {

    @Upsert
    fun upsertPlayer(player: Player)

    @Query("UPDATE player SET score=:score WHERE direction=:direction")
    fun updatePlayerScore(score: Int, direction: Int)

    @Query("DELETE FROM player")
    fun deleteAllPlayer()

    @Query("SELECT * FROM player WHERE direction=:direction")
    fun getPlayerByDirection(direction: Int): Player?
}