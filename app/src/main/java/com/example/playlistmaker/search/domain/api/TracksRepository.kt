package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Resource
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface TracksRepository {
    fun getSearchTracks(exception: String): Flow<Resource<List<Track>>>

    fun getSearchHistory(): Flow<List<Track>>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
}