package com.example.playlistmaker.db.domain.impl

import com.example.playlistmaker.db.domain.api.FavoriteTracksInteractor
import com.example.playlistmaker.db.domain.api.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(private val repository:FavoriteTracksRepository):FavoriteTracksInteractor{
    override suspend fun addToFavorite(track: Track) {
        repository.addToFavorite(track)
    }

    override suspend fun deleteFromFavorite(track: Track) {
        repository.deleteFromFavorite(track)
    }

    override fun getTracks(): Flow<List<Track>> {
        return repository.getTracks()
    }

    override suspend fun getTracksId(): List<String> {
        return repository.getTracksId()
    }
}