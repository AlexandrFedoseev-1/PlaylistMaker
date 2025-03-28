package com.example.playlistmaker.player



sealed interface AddPlaylistResult {
    data class Success(val message: String) : AddPlaylistResult
    data class Error(val message: String) : AddPlaylistResult
}