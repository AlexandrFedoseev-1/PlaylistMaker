package com.example.playlistmaker.ui.settings


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.api.SettingsInteractor


class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsInteractor: SettingsInteractor

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingsInteractor = Creator.provideSettingsInteractor()
        binding.toolbar.setNavigationOnClickListener { super.finish() }


        binding.themeSwitcher.isChecked = settingsInteractor.isDarkThemeEnabled()!!
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)

        }

        binding.share.setOnClickListener {
            settingsInteractor.shareApp()
        }

        binding.support.setOnClickListener {
            settingsInteractor.writeToSupport()
        }

        binding.userAgreement.setOnClickListener {
            settingsInteractor.showUserAgreement()
        }
    }
}