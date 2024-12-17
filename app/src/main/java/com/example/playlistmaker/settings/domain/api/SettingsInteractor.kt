package com.example.playlistmaker.settings.domain.api

interface SettingsInteractor {
    fun isDarkThemeEnabled(): Boolean?
    fun saveTheme(darkThemeEnabled: Boolean)
    fun switchTheme(darkThemeEnabled: Boolean)
    fun shareApp()
    fun writeToSupport()
    fun showUserAgreement()
}