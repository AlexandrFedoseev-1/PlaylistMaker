package com.example.playlistmaker.settings.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { super.finish() }

        viewModel.getDarkThemeLiveData().observe(this){
            binding.themeSwitcher.isChecked =it
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)


        }

        binding.share.setOnClickListener {
            viewModel.shareApp()
        }

        binding.support.setOnClickListener {
            viewModel.writeToSupport()
        }

        binding.userAgreement.setOnClickListener {
            viewModel.showUserAgreement()
        }
    }
}