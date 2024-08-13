package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSearch = findViewById<Button>(R.id.search)
        val buttonMediaLib = findViewById<Button>(R.id.media_lib)
        val buttonSettings = findViewById<Button>(R.id.settings)
        buttonSearch.setOnClickListener{
            val searchIntent = Intent(this,SearchActivity::class.java)
            startActivity(searchIntent)
        }
        buttonMediaLib.setOnClickListener{
            val mediaLibIntent = Intent(this,MediaLibActivity::class.java)
            startActivity(mediaLibIntent)
        }
        buttonSettings.setOnClickListener{
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

    }
}