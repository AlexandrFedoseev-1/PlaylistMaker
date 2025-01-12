package com.example.playlistmaker.media_lib.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibBinding
    private val viewPagerAdapter by lazy { MediaLibViewPagerAdapter(supportFragmentManager,lifecycle) }
    private lateinit var tabMediator : TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { super.finish() }

        binding.viewPager.adapter = viewPagerAdapter
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager){
            tab, position ->
            when (position){
                0 -> tab.text = getString(R.string.favorites_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

}