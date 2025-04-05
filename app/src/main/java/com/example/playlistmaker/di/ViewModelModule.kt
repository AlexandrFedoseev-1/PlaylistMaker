package com.example.playlistmaker.di

import com.example.playlistmaker.media_lib.ui.add_playlist.AddPlaylistViewModel
import com.example.playlistmaker.media_lib.ui.edit_playlist.EditPlaylistViewModel
import com.example.playlistmaker.media_lib.ui.favorites.FavoritesTracksViewModel
import com.example.playlistmaker.media_lib.ui.list_playlists.ListPlaylistViewModel
import com.example.playlistmaker.media_lib.ui.list_playlists.playlist.PlaylistViewModel
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
        ListPlaylistViewModel(get())
    }

    viewModel {
        AddPlaylistViewModel(get())
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel {
        EditPlaylistViewModel(get())
    }


}