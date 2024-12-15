package com.example.playlistmaker.data

import com.example.playlistmaker.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val settingsLocalDataSource: SettingsLocalDataSource): SettingsRepository {

    override fun isDarkThemeEnabled(): Boolean? {
        return settingsLocalDataSource.isDarkThemeEnabled()
    }

    override fun saveTheme(darkThemeEnabled: Boolean) {
        settingsLocalDataSource.saveTheme(darkThemeEnabled)
    }

    override fun shareApp() {
        settingsLocalDataSource.shareApp()
    }

    override fun writeToSupport() {
        settingsLocalDataSource.writeToSupport()
    }

    override fun showUserAgreement() {
        settingsLocalDataSource.showUserAgreement()
    }
}