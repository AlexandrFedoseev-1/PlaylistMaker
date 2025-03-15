package com.example.playlistmaker.media_lib.ui.fragments

import com.example.playlistmaker.search.domain.models.Track

sealed interface  FavoriteState {
    data object Placeholder:FavoriteState
    data class TrackList( val tracks: List<Track>): FavoriteState
}