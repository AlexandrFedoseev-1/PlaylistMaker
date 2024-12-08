package com.example.playlistmaker.data

import com.example.playlistmaker.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val settingsManager: SettingsManager): SettingsRepository {

    override fun isDarkThemeEnabled(): Boolean? {
        return settingsManager.isDarkThemeEnabled()
    }

    override fun saveTheme(darkThemeEnabled: Boolean) {
        settingsManager.saveTheme(darkThemeEnabled)
    }

    override fun shareApp() {
        settingsManager.shareApp()
    }

    override fun writeToSupport() {
        settingsManager.writeToSupport()
    }

    override fun showUserAgreement() {
        settingsManager.showUserAgreement()
    }
}