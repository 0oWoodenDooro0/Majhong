package com.example.majhong.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PlayerDao {

    @Upsert
    fun upsertPlayer(player: Player)

    @Query("UPDATE player SET direction=:direction WHERE id=:id")
    fun updatePlayerDirectionById(direction: Int, id: Int)

    @Query("UPDATE player SET score=:score WHERE direction=:direction")
    fun updatePlayerScoreByDirection(score: Int, direction: Int)

    @Query("UPDATE player SET score=:score WHERE id=:id")
    fun updatePlayerScoreById(score: Int, id: Int)

    @Query("DELETE FROM player")
    fun deleteAllPlayer()

    @Query("UPDATE player SET score=0")
    fun setAllPlayerScoreToZero()

    @Query("SELECT * FROM player WHERE direction=:direction")
    fun getPlayerByDirection(direction: Int): Player?

    @Query("SELECT * FROM player WHERE id=:id")
    fun getPlayerById(id: Int): Player

    @Query("SELECT * FROM player")
    fun getAllPlayer(): List<Player>
}