package com.example.playlistmaker.player.ui

sealed class AudioPlayerScreenState {
    data object Default : AudioPlayerScreenState()
    data object Prepared : AudioPlayerScreenState()
    data class Playing(val currentPosition: String) : AudioPlayerScreenState()
    data class Paused(val currentPosition: String) : AudioPlayerScreenState()
}