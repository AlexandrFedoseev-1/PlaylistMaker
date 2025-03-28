package com.example.playlistmaker.media_lib.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import com.example.playlistmaker.databinding.FragmentFavoritesTracksBinding
import com.example.playlistmaker.debounce
import com.example.playlistmaker.media_lib.ui.FavoriteAdapter
import com.example.playlistmaker.media_lib.ui.fragments.viewModel.FavoritesTracksViewModel
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTracksFragment : Fragment() {
    companion object {
        fun newInstance() = FavoritesTracksFragment()
        private const val CLICK_DEBOUNCE_DELAY = 300L

    }

    private lateinit var binding: FragmentFavoritesTracksBinding
    private val viewModel by viewModel<FavoritesTracksViewModel>()
    private val favoriteAdapter by lazy { FavoriteAdapter{track ->
        onTrackClickDebounce(track)
    } }
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favoriteTracks.adapter = favoriteAdapter

        onTrackClickDebounce =
            debounce(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { track ->
                val action = FavoritesTracksFragmentDirections.actionGlobalAudioPlayerFragment(track)
                findNavController().navigate(action)
            }

        viewModel.favoriteState.observe(viewLifecycleOwner){ state ->
            when(state){
                is FavoriteState.Placeholder -> setScreenState(state)
                is FavoriteState.TrackList -> {
                    favoriteAdapter.updateData(state.tracks)
                    setScreenState(state)
                }
            }

        }


    }
    private fun setScreenState(state: FavoriteState) {
        binding.favoriteTracks.isVisible = state is FavoriteState.TrackList
        binding.placeholder.isVisible = state is FavoriteState.Placeholder
    }


}