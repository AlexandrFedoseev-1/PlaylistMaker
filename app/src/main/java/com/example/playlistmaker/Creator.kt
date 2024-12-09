package com.example.playlistmaker


import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.SearchHistoryManager
import com.example.playlistmaker.data.SettingsManager
import com.example.playlistmaker.data.SettingsRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.domain.api.AudioPlayer
import com.example.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.SettingsInteractor
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl



object Creator {
    private lateinit var appContext: Context

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(), getSearchHistoryManger())
    }
    private fun getSearchHistoryManger(): SearchHistoryManager{
        return SearchHistoryManager(appContext)
    }

    fun provideTracksInteractor(): TracksInteractor{
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideSettingsInteractor(): SettingsInteractor{
        return SettingsInteractorImpl(getSettingsRepository())
    }
    private fun getSettingsRepository():SettingsRepository{
        return SettingsRepositoryImpl(getSettingsManager())
    }

    private fun getSettingsManager():SettingsManager{
        return SettingsManager(appContext)
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor{
        return AudioPlayerInteractorImpl(getAudioPlayer())
    }

    private fun getAudioPlayer():AudioPlayer{
        return MediaPlayerImpl(MediaPlayer())
    }


}