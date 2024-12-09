package com.example.playlistmaker.domain.api

interface AudioPlayer {
    fun prepare(previewUrl: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun start()
    fun pause()
    fun stop()
    fun getCurrentPosition(): String
    fun release()
}
