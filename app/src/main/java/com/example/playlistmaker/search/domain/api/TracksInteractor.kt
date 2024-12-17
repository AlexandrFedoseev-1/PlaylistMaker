package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.consumer.Consumer
import com.example.playlistmaker.search.domain.models.Track


interface TracksInteractor {
    fun searchTracks(exception: String, consumer: Consumer<List<Track>>)

    fun getSearchHistory(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
}