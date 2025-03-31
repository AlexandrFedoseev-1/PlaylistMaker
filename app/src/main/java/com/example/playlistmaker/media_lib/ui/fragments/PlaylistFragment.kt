package com.example.playlistmaker.media_lib.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media_lib.ui.PlaylistAdapter
import com.example.playlistmaker.media_lib.ui.fragments.viewModel.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    companion object {
        fun newInstance() = PlaylistFragment()
    }

    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel by viewModel<PlaylistViewModel>()
    private val playlistAdapter by lazy { PlaylistAdapter()  }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycleViewPlayList.adapter = playlistAdapter

        binding.newPlaylistButton.setOnClickListener{
            findNavController().navigate(R.id.action_mediaLibFragment_to_addPlaylistFragment)
        }

        viewModel.playlists.observe(viewLifecycleOwner){ playlists ->
            playlistAdapter.updateData(playlists)
            binding.placeholder.isVisible =  playlists.isEmpty()
        }
    }
}