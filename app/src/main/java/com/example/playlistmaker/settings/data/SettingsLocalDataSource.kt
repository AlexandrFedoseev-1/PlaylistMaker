package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.App
import com.example.playlistmaker.R

class SettingsLocalDataSource(
    private val context: Context,
    private val sharedPref: SharedPreferences
) {

    fun isDarkThemeEnabled(): Boolean? {
        val theme = sharedPref.getString(App.DARK_THEME, null)
        return theme?.toBooleanStrictOrNull()
    }

    fun saveTheme(darkThemeEnabled: Boolean) {
        sharedPref.edit().putString(App.DARK_THEME, darkThemeEnabled.toString()).apply()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        saveTheme(darkThemeEnabled)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_massage_subject))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_massage))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Добавляем флаг
        }
        context.startActivity(shareIntent)
    }

    fun writeToSupport() {
        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(
                "mailto:" + context.getString(R.string.support_mail) +
                        "?&subject=" + Uri.encode(context.getString(R.string.support_subject)) +
                        "&body=" + Uri.encode(context.getString(R.string.support_massage))
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Добавляем флаг
        }
        context.startActivity(supportIntent)
    }

    fun showUserAgreement() {
        val userAgreementIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(context.getString(R.string.user_agreement_url))
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Добавляем флаг
        }
        context.startActivity(userAgreementIntent)
    }

}