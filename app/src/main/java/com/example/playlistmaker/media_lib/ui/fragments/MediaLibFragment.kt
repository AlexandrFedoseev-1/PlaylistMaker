package com.example.playlistmaker.media_lib.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaLibBinding
import com.example.playlistmaker.media_lib.ui.MediaLibViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class MediaLibFragment : Fragment() {

    private lateinit var viewPagerAdapter: MediaLibViewPagerAdapter
    private lateinit var tabMediator : TabLayoutMediator
    private lateinit var binding: FragmentMediaLibBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaLibBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter = MediaLibViewPagerAdapter(childFragmentManager,lifecycle)
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

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }

    companion object {

        fun newInstance() =
            MediaLibFragment()
    }
}