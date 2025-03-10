package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface TracksInteractor {
    fun searchTracks(exception: String): Flow<Pair<List<Track>?, String?>>

    fun getSearchHistory(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
}