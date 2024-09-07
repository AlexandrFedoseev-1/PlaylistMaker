package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val sharedPref = getSharedPreferences(SETTING_PREFERENCES, MODE_PRIVATE)
        val theme = sharedPref.getString(DARK_THEME, null)
        if (!theme.isNullOrEmpty()) {
            darkTheme = theme.toBoolean()
            AppCompatDelegate.setDefaultNightMode(
                if (darkTheme) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }
        else {

            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            darkTheme = when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_YES -> true
                Configuration.UI_MODE_NIGHT_NO -> false
                else -> false
            }
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        val sharedPref = getSharedPreferences(SETTING_PREFERENCES, MODE_PRIVATE)
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPref.edit().putString(DARK_THEME, darkTheme.toString()).apply()
    }

    companion object {
        const val SETTING_PREFERENCES = "setting_preferences"
        const val DARK_THEME = "dark_theme"
        var darkTheme = false
    }
}
