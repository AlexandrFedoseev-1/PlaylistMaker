package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Resource
import com.example.playlistmaker.search.domain.models.Track


interface TracksRepository {
    fun getSearchTracks(exception: String): Resource<List<Track>>

    fun getSearchHistory(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
}