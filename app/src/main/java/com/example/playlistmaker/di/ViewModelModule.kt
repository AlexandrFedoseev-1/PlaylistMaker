package com.example.playlistmaker.di

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

    viewModel{(previewUrl:String)->
        AudioPlayerViewModel(get(), previewUrl = previewUrl )
    }
}