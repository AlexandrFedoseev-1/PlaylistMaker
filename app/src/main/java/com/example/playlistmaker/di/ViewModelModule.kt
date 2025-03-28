package com.example.playlistmaker.di

import com.example.playlistmaker.media_lib.ui.fragments.viewModel.AddPlaylistViewModel
import com.example.playlistmaker.media_lib.ui.fragments.viewModel.FavoritesTracksViewModel
import com.example.playlistmaker.media_lib.ui.fragments.viewModel.PlaylistViewModel
import com.example.playlistmaker.player.ui.AudioPlayerViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }

    viewModel{
        SettingsViewModel(get())
    }

    viewModel{
        AudioPlayerViewModel(get(), favoriteTracks = get(), get() )
    }

    viewModel {
        FavoritesTracksViewModel(favoriteTracksInteractor = get())
    }

    viewModel{
        PlaylistViewModel(get())
    }

    viewModel {
        AddPlaylistViewModel(get())
    }


}