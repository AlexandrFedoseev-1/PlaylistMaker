package com.example.playlistmaker.ui.settings


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.SettingsInteractor
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsInteractor: SettingsInteractor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backButton = findViewById<ImageView>(R.id.back_button)
        val sharedButton = findViewById<TextView>(R.id.share)
        val supportButton = findViewById<TextView>(R.id.support)
        val userAgreementButton = findViewById<TextView>(R.id.user_agreement)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.theme_switcher)

        settingsInteractor = Creator.provideSettingsInteractor()

        backButton.setOnClickListener {
            super.finish()
        }

        themeSwitcher.isChecked = settingsInteractor.isDarkThemeEnabled()!!
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)

        }

        sharedButton.setOnClickListener {
            settingsInteractor.shareApp()
        }

        supportButton.setOnClickListener {
            settingsInteractor.writeToSupport()
        }

        userAgreementButton.setOnClickListener {
            settingsInteractor.showUserAgreement()
        }
    }
}