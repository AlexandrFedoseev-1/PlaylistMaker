package com.example.playlistmaker.creator


import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.search.data.SearchHistoryLocalDataSource
import com.example.playlistmaker.settings.data.SettingsLocalDataSource
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.TracksRepositoryImpl
import com.example.playlistmaker.player.domain.api.AudioPlayer
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl



object Creator {
    private lateinit var appContext: Context
    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        appContext = context.applicationContext
        sharedPreferences = appContext.getSharedPreferences(SearchHistoryLocalDataSource.SEARCH_HISTORY_PREFERENCES,Context.MODE_PRIVATE)
    }
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(), getSearchHistoryManger())
    }
    private fun getSearchHistoryManger(): SearchHistoryLocalDataSource {
        return SearchHistoryLocalDataSource(sharedPreferences)
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }
    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(getSettingsManager())
    }

    private fun getSettingsManager(): SettingsLocalDataSource {
        return SettingsLocalDataSource(appContext)
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(getAudioPlayer())
    }

    private fun getAudioPlayer(): AudioPlayer {
        return MediaPlayerRepositoryImpl(MediaPlayer())
    }


}