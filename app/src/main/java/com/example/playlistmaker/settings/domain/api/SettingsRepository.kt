package com.example.playlistmaker.settings.domain.api

interface SettingsRepository {
    fun isDarkThemeEnabled(): Boolean?
    fun saveTheme(darkThemeEnabled: Boolean)
    fun switchTheme(darkThemeEnabled: Boolean)
    fun shareApp()
    fun writeToSupport()
    fun showUserAgreement()
}