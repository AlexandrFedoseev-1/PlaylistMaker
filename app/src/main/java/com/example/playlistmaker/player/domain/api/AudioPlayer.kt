package com.example.playlistmaker.player.domain.api

interface AudioPlayer {
    val isPlaying: Boolean
    fun prepare(previewUrl: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun start()
    fun pause()
    fun stop()
    fun getCurrentPosition(): String
    fun release()
}
