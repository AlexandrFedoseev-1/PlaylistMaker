package com.example.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor


class AudioPlayerViewModel(
    private val player: AudioPlayerInteractor,
    private val previewUrl: String
) : ViewModel() {


    private val _playerState =
        MutableLiveData<AudioPlayerScreenState>(AudioPlayerScreenState.Default)
    val playerState: LiveData<AudioPlayerScreenState> get() = _playerState


    private val handler = Handler(Looper.getMainLooper())
    private var playbackRunnable: Runnable

    init {
        playbackRunnable = Runnable { updatePlaybackTime() }
        playerPrepare()
    }


    private fun playerPrepare() {
        player.prepare(previewUrl,
            onPrepared = {
                _playerState.value = AudioPlayerScreenState.Prepared
            },
            onCompletion = {
                handler.removeCallbacks(playbackRunnable)
                _playerState.value = AudioPlayerScreenState.Paused("00:00")
                _playerState.value = AudioPlayerScreenState.Prepared
            }
        )
    }


    fun startPlayer() {
        player.start()
        handler.post(playbackRunnable)
        _playerState.value = AudioPlayerScreenState.Playing(player.getCurrentPosition())
    }

    fun pausePlayer() {
        handler.removeCallbacks(playbackRunnable)
        player.pause()
        _playerState.value = AudioPlayerScreenState.Paused(player.getCurrentPosition())
    }


    private fun updatePlaybackTime() {
        _playerState.value = when (_playerState.value) {
            is AudioPlayerScreenState.Playing -> AudioPlayerScreenState.Playing(player.getCurrentPosition())
            is AudioPlayerScreenState.Paused -> AudioPlayerScreenState.Paused(player.getCurrentPosition())
            else -> _playerState.value
        }
        handler.postDelayed(playbackRunnable, DELAY)
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
        handler.removeCallbacks(playbackRunnable)
    }

    companion object {

        const val DELAY = 300L
    }
}