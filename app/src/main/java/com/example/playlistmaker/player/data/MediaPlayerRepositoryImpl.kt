package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.AudioPlayer
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : AudioPlayer {

    override fun prepare(previewUrl: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.apply {
            reset()
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener { onPrepared() }
            setOnCompletionListener { onCompletion() }

        }
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun stop() {
        mediaPlayer.stop()
    }

    override fun getCurrentPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(mediaPlayer.currentPosition)
    }

    override fun release() {
        mediaPlayer.release()
    }
}
