package com.example.playlistmaker.db.domain.api

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {
    suspend fun addToFavorite(track: Track)
    suspend fun deleteFromFavorite(track: Track)
    fun getTracks(): Flow<List<Track>>
}