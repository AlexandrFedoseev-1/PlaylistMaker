package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.api.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository): SettingsInteractor {

    override fun isDarkThemeEnabled(): Boolean? {
        return repository.isDarkThemeEnabled()
    }

    override fun saveTheme(darkThemeEnabled: Boolean) {
        repository.saveTheme(darkThemeEnabled)
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        repository.switchTheme(darkThemeEnabled)
    }

    override fun shareApp() {
        repository.shareApp()
    }

    override fun writeToSupport() {
        repository.writeToSupport()
    }

    override fun showUserAgreement() {
        repository.showUserAgreement()
    }


}