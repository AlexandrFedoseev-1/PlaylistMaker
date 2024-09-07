package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backButton = findViewById<ImageView>(R.id.back_button)
        val sharedButton = findViewById<TextView>(R.id.share)
        val supportButton = findViewById<TextView>(R.id.support)
        val userAgreementButton = findViewById<TextView>(R.id.user_agreement)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.theme_switcher)

        backButton.setOnClickListener {
            super.finish()
        }

        themeSwitcher.isChecked = App.darkTheme
        themeSwitcher.setOnCheckedChangeListener { swither, checked ->
            (applicationContext as App).switchTheme(checked)

        }

        sharedButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_massage_subject))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_massage))
            startActivity(shareIntent)
        }

        supportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.setData(
                Uri.parse(
                    "mailto:" + getString(R.string.support_mail)
                            + "?&subject=" + Uri.encode(getString(R.string.support_subject)) +
                            "&body=" + Uri.encode(getString(R.string.support_massage))
                )
            )
            startActivity(supportIntent)
        }

        userAgreementButton.setOnClickListener {
            val userAgreementIntent = Intent(Intent.ACTION_VIEW , Uri.parse(getString(R.string.user_agreement_url)))
            startActivity(userAgreementIntent)
        }
    }
}