package com.example.playlistmaker.db.domain.api

import com.example.playlistmaker.media_lib.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    fun getPlaylist(): Flow<List<Playlist>>
    suspend fun getPlaylistTracksId(): List<String>
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) : String
}