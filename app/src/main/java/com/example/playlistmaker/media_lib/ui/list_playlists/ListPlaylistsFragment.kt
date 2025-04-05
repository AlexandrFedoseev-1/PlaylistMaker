package com.example.playlistmaker.media_lib.ui.list_playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentListPlaylistsBinding
import com.example.playlistmaker.debounce
import com.example.playlistmaker.media_lib.domain.model.Playlist
import com.example.playlistmaker.media_lib.ui.MediaLibFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListPlaylistsFragment : Fragment() {
    companion object {
        fun newInstance() = ListPlaylistsFragment()
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

    private lateinit var binding: FragmentListPlaylistsBinding
    private val viewModel by viewModel<ListPlaylistViewModel>()
    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit
    private val listPlaylistAdapter by lazy { ListPlaylistAdapter{ playlist ->
        onPlaylistClickDebounce(playlist)
    }  }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycleViewPlayList.adapter = listPlaylistAdapter

        binding.newPlaylistButton.setOnClickListener{
            findNavController().navigate(R.id.action_mediaLibFragment_to_addPlaylistFragment)
        }
        onPlaylistClickDebounce =
            debounce(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { playlist ->
                val action = MediaLibFragmentDirections.actionMediaLibFragmentToPlaylistFragment(playlist)
                findNavController().navigate(action)
            }
        viewModel.playlists.observe(viewLifecycleOwner){ playlists ->
            listPlaylistAdapter.updateData(playlists)
            binding.placeholder.isVisible =  playlists.isEmpty()
        }
    }
}