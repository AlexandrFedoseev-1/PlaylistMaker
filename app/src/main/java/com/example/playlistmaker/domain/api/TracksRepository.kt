package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track


interface TracksRepository {
    fun getSearchTracks(exception: String): Resource<List<Track>>

    fun getSearchHistory(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
}