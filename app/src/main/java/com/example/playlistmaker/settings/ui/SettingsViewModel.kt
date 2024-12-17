package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.api.SettingsInteractor

class SettingsViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    private var darkThemeLiveData = MutableLiveData(isDarkThemeEnabled())

    private fun isDarkThemeEnabled(): Boolean {
        return settingsInteractor.isDarkThemeEnabled()!!
    }

    fun getDarkThemeLiveData(): LiveData<Boolean> = darkThemeLiveData

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkThemeLiveData.value = darkThemeEnabled
        settingsInteractor.switchTheme(darkThemeEnabled)
    }

    fun shareApp() {
        settingsInteractor.shareApp()
    }

    fun writeToSupport() {
        settingsInteractor.writeToSupport()
    }

    fun showUserAgreement() {
        settingsInteractor.showUserAgreement()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    Creator.provideSettingsInteractor()
                )
            }
        }
    }
}