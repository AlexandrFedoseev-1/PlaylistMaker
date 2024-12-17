package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.models.Track

sealed class ScreenState {
    data object Empty : ScreenState()
    data object Loading : ScreenState()
    data class SearchHistory(val history: List<Track>) : ScreenState()
    data class SearchResults(val results: List<Track>) : ScreenState()
    data class Placeholder(val massage: String) : ScreenState()
    companion object{
        const val NOT_FOUND = "NOT_FOUND"
        const val NO_INTERNET = "NO_INTERNET"
    }
}
