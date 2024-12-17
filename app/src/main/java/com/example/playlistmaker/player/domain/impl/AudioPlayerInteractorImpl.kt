package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.AudioPlayer
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor

class AudioPlayerInteractorImpl(private val player: AudioPlayer): AudioPlayerInteractor {


    override fun prepare(previewUrl: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        player.prepare(previewUrl,onPrepared,onCompletion)
    }

    override fun start() {
        player.start()
    }

    override fun pause() {
        player.pause()
    }

    override fun stop() {
        player.stop()
    }

    override fun getCurrentPosition(): String {
        return player.getCurrentPosition()
    }

    override fun release() {
        player.release()
    }

}