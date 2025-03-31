package com.example.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.db.data.entity.AddTracksToPlaylistsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddTracksToPlaylistsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: AddTracksToPlaylistsEntity)

    @Delete
    suspend fun deleteTrack(track: AddTracksToPlaylistsEntity)

    @Query("SELECT * FROM add_tracks_to_playlist WHERE trackId IN (:trackIds)")
    fun getTracksById(trackIds: List<String>): Flow<List<AddTracksToPlaylistsEntity>>

    @Query("DELETE FROM add_tracks_to_playlist WHERE trackId = :id")
    suspend fun deleteTrackById(id: String)

}