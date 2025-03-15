package com.example.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.db.data.FavoriteTracksEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTracksDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteTracksEntity)

    @Delete
    suspend fun deleteTrack(track: FavoriteTracksEntity)

    @Query("SELECT * FROM favorite_tracks")
    fun getTracks(): Flow<List<FavoriteTracksEntity>>

    @Query("SELECT trackId FROM favorite_tracks")
    suspend fun getTracksId():List<String>
}