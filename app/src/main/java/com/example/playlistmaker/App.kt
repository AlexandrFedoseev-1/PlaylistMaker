package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {
    private val settingsInteractor: SettingsInteractor by inject()
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@App)
            modules(dataModule, interactorModule, viewModelModule)
        }

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
