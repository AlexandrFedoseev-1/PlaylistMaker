package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.models.Track


interface TracksInteractor {
    fun searchTracks(exception: String, consumer: Consumer<List<Track>>)

    fun getSearchHistory(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
}