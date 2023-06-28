package com.example.majhong.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PlayerDao {

    @Upsert
    fun upsertPlayer(player: Player)

    @Query("UPDATE player SET score=:score WHERE direction=:direction")
    fun updatePlayerScore(score: Int, direction: Int)

    @Delete
    fun deletePlayer(player: Player)

    @Query("SELECT * FROM player WHERE direction=:direction")
    fun getPlayerByDirection(direction: Int): Player?
}