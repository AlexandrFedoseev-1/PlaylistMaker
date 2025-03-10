package com.example.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AudioPlayerViewModel(
    private val player: AudioPlayerInteractor,
    private val previewUrl: String
) : ViewModel() {

    private var timerJob: Job? = null


    private val _playerState =
        MutableLiveData<AudioPlayerScreenState>(AudioPlayerScreenState.Default())
    val playerState: LiveData<AudioPlayerScreenState> get() = _playerState


    init {
        playerPrepare()
    }


    private fun playerPrepare() {
        player.prepare(previewUrl,
            onPrepared = {
                _playerState.value = AudioPlayerScreenState.Prepared()
            },
            onCompletion = {
                timerJob?.cancel()
                _playerState.value = AudioPlayerScreenState.Prepared()
            }
        )
    }


    private fun startPlayer() {
        player.start()
        _playerState.value = AudioPlayerScreenState.Playing(player.getCurrentPosition())
        startTimer()
    }

    fun pausePlayer() {
        player.pause()
        timerJob?.cancel()
        _playerState.value = AudioPlayerScreenState.Paused(player.getCurrentPosition())
    }


    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (player.isPlaying) {
                delay(DELAY)
                _playerState.value = AudioPlayerScreenState.Playing(player.getCurrentPosition())
            }
        }
    }

    fun playbackControl() {
        when (playerState.value) {
            is AudioPlayerScreenState.Playing -> pausePlayer()
            is AudioPlayerScreenState.Prepared, is AudioPlayerScreenState.Paused -> startPlayer()
            else -> Unit
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.stop()
        player.release()
    }

    companion object {

        const val DELAY = 300L
    }
}