package com.example.playlistmaker.player.domain.api

interface AudioPlayerInteractor {
    fun prepare(previewUrl: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun start()
    fun pause()
    fun stop()
    fun getCurrentPosition(): String
    fun release()
}