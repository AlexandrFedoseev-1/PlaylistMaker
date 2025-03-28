package com.example.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.db.data.entity.AddTracksToPlaylistsEntity

@Dao
interface AddTracksToPlaylistsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: AddTracksToPlaylistsEntity)

    @Delete
    suspend fun deleteTrack(track: AddTracksToPlaylistsEntity)
}