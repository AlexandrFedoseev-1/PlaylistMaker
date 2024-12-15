package com.example.playlistmaker.domain.api

interface SettingsInteractor {
    fun isDarkThemeEnabled(): Boolean?
    fun saveTheme(darkThemeEnabled: Boolean)
    fun shareApp()
    fun writeToSupport()
    fun showUserAgreement()
}