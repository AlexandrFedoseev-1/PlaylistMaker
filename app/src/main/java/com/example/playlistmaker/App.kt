package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.api.SettingsInteractor

class App : Application() {
    private lateinit var settingsInteractor: SettingsInteractor
    override fun onCreate() {
        super.onCreate()
        Creator.initialize(this)
        settingsInteractor = Creator.provideSettingsInteractor()

        val theme = settingsInteractor.isDarkThemeEnabled()
        if (theme != null) {
            AppCompatDelegate.setDefaultNightMode(
                if (theme) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        } else {

            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val darkTheme = when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_YES -> true
                Configuration.UI_MODE_NIGHT_NO -> false
                else -> false
            }
            settingsInteractor.saveTheme(darkTheme)
        }
    }


    companion object {
        const val SETTING_PREFERENCES = "setting_preferences"
        const val DARK_THEME = "dark_theme"
    }
}
