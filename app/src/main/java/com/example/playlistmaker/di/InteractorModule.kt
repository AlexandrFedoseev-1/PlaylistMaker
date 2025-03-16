package com.example.playlistmaker.di

import com.example.playlistmaker.db.domain.Impl.FavoriteTracksInteractorImpl
import com.example.playlistmaker.db.domain.api.FavoriteTracksInteractor
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(player = get())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(repository = get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(repository = get())
    }

    factory<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get())
    }
}


