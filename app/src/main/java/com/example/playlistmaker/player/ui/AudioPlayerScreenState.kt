package com.example.playlistmaker.player.ui

import androidx.annotation.DrawableRes
import com.example.playlistmaker.R

sealed class AudioPlayerScreenState(val isPlayButtonEnabled: Boolean, @DrawableRes val buttonImage: Int, val progress: String) {
    class Default : AudioPlayerScreenState(false, R.drawable.play_button,"00:00")
    class Prepared : AudioPlayerScreenState(true, R.drawable.play_button,"00:00")
    class Playing(progress: String) : AudioPlayerScreenState(true, R.drawable.pause_button, progress )
    class Paused(progress: String) : AudioPlayerScreenState(true, R.drawable.play_button, progress )
}