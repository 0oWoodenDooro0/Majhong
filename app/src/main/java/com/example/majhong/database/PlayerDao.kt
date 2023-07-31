package com.example.majhong.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PlayerDao {

    @Upsert
    suspend fun upsertPlayer(player: Player)

    @Query("UPDATE player SET direction=:direction WHERE id=:id")
    suspend fun updatePlayerDirectionById(id: Int, direction: Int)

    @Query("UPDATE player SET score=:score WHERE id=:id")
    suspend fun updatePlayerScoreById(id: Int, score: Int)

    @Query("UPDATE player SET winCount=:count WHERE id=:id")
    suspend fun updatePlayerWinCountById(id: Int, count: Int)

    @Query("UPDATE player SET selfDrawnCount=:count WHERE id=:id")
    suspend fun updatePlayerSelfDrawnCountById(id: Int, count: Int)

    @Query("UPDATE player SET chunkCount=:count WHERE id=:id")
    suspend fun updatePlayerChunkCountById(id: Int, count: Int)

    @Query("DELETE FROM player")
    suspend fun deleteAllPlayer()

    @Query("UPDATE player SET score=0")
    suspend fun setAllPlayerScoreToZero()

    @Query("SELECT * FROM player WHERE id=:id")
    suspend fun getPlayerById(id: Int): Player

    @Query("SELECT * FROM player")
    suspend fun getAllPlayer(): List<Player>
}