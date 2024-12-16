package com.example.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor


class AudioPlayerViewModel(private val player: AudioPlayerInteractor, private val previewUrl: String) : ViewModel() {



    private val _playerState = MutableLiveData(STATE_DEFAULT)
    val playerState: LiveData<Int> get() = _playerState

    private val _currentPosition = MutableLiveData("00:00")
    val currentPosition: LiveData<String> get() = _currentPosition

    private val handler = Handler(Looper.getMainLooper())
    private var playbackRunnable: Runnable

    init {
        playbackRunnable = Runnable { updatePlaybackTime() }
        playerPrepare()
    }


    private fun playerPrepare() {
        player.prepare(previewUrl,
            onPrepared = {
                _playerState.value = STATE_PREPARED
            },
            onCompletion = {
                handler.removeCallbacks(playbackRunnable)
                _currentPosition.value = "00:00"
                _playerState.value = STATE_PREPARED
            }
        )
    }


    fun startPlayer() {
        player.start()
        handler.post(playbackRunnable)
//        binding.playButton.setBackgroundResource(R.drawable.pause_button)
        _playerState.value = STATE_PLAYING
    }

    fun pausePlayer() {
        handler.removeCallbacks(playbackRunnable)
        player.pause()
//        binding.playButton.setBackgroundResource(R.drawable.play_button)
        _playerState.value = STATE_PAUSED
    }


    private fun updatePlaybackTime() {
        _currentPosition.value = player.getCurrentPosition()
        handler.postDelayed(playbackRunnable, DELAY)
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
        handler.removeCallbacks(playbackRunnable)
    }

    companion object {
        fun getViewModelFactory( previewUrl: String ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AudioPlayerViewModel(
                    Creator.provideAudioPlayerInteractor(),
                    previewUrl
                )
            }
        }

        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val DELAY = 300L
    }
}