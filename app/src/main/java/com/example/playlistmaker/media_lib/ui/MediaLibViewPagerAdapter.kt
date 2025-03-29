package com.example.playlistmaker.media_lib.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.media_lib.ui.favorites.FavoritesTracksFragment
import com.example.playlistmaker.media_lib.ui.list_playlists.ListPlaylistsFragment

class MediaLibViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) FavoritesTracksFragment.newInstance()
        else ListPlaylistsFragment.newInstance()
    }

}