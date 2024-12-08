package com.example.playlistmaker.domain.api

interface SettingsRepository {
    fun isDarkThemeEnabled(): Boolean?
    fun saveTheme(darkThemeEnabled: Boolean)
    fun shareApp()
    fun writeToSupport()
    fun showUserAgreement()
}