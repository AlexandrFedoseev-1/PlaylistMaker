package com.example.playlistmaker.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.App
import com.example.playlistmaker.R

class SettingsLocalDataSource(private val context: Context) {
    private val sharedPref = context.getSharedPreferences(App.SETTING_PREFERENCES, Context.MODE_PRIVATE)
    fun isDarkThemeEnabled(): Boolean? {
        val theme = sharedPref.getString(App.DARK_THEME, null)
        return theme?.toBoolean()
    }

    fun saveTheme(darkThemeEnabled: Boolean) {
        sharedPref.edit().putString(App.DARK_THEME, darkThemeEnabled.toString()).apply()
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
        val userAgreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.user_agreement_url))).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Добавляем флаг
        }
        context.startActivity(userAgreementIntent)
    }

}