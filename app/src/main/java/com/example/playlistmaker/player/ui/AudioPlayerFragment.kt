package com.example.playlistmaker.player.ui


import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.BottomSheetPlaylistsBinding
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.debounce
import com.example.playlistmaker.media_lib.domain.model.Playlist
import com.example.playlistmaker.player.AddPlaylistResult
import com.example.playlistmaker.player.BottomSheetPlaylistAdapter
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class AudioPlayerFragment : Fragment() {
    private val viewModel by viewModel<AudioPlayerViewModel>()

    private lateinit var binding: FragmentAudioPlayerBinding
    private lateinit var bottomSheetBinding: BottomSheetPlaylistsBinding

    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit
    private val args: AudioPlayerFragmentArgs by navArgs()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val bottomSheetPlaylistAdapter by lazy {
        BottomSheetPlaylistAdapter { playlist ->
            onPlaylistClickDebounce(playlist)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        bottomSheetBinding =
            BottomSheetPlaylistsBinding.bind(binding.root.findViewById(R.id.bottom_sheet_new_playlist))

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val track: Track = args.track
        viewModel.setValues(track.trackId, track.previewUrl.toString())

        setupTrackInfo(track)

        setupBottomSheet()

        bottomSheetBinding.rvPlaylists.adapter = bottomSheetPlaylistAdapter
        onPlaylistClickDebounce =
            debounce(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { playlist ->
                viewModel.addTrackToPlaylist(track, playlist)
            }

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.addFavoritesButton.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        viewModel.playerState.observe(viewLifecycleOwner) { state ->
            binding.playButton.isEnabled = state.isPlayButtonEnabled
            binding.playButton.setBackgroundResource(state.buttonImage)
            binding.demoPlayTime.text = state.progress
        }

        viewModel.isFavoriteLiveData.observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                binding.addFavoritesButton.setBackgroundResource(R.drawable.add_favorite_button_on)
            } else {
                binding.addFavoritesButton.setBackgroundResource(R.drawable.add_favorites_button)
            }
        }

        binding.addPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        viewModel.playlists.observe(viewLifecycleOwner) { playlist ->
            bottomSheetPlaylistAdapter.updateData(playlist)
        }
        viewModel.addTrackStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is AddPlaylistResult.Success -> {
                    showSnackBar(getString(R.string.add_track_to_playlist) + " '${status.message}'")
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }

                is AddPlaylistResult.Error -> showSnackBar(getString(R.string.already_add) + " '${status.message}'")
            }
        }

        bottomSheetBinding.buttonNewListBottomSheet.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_addPlaylistFragment)
        }
    }

    private fun showSnackBar(message: String?) {
        message?.let {
            val snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
            val textView =
                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            val typeface = ResourcesCompat.getFont(requireContext(), R.font.ys_display_regular)
            textView.textSize = 16f
            textView.setTypeface(typeface)
            textView.gravity = Gravity.CENTER
            snackbar.show()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetBinding.root as View).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.overlay.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.overlay.visibility =
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) View.GONE else View.VISIBLE
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = ((slideOffset + 1) / 2).coerceIn(0f, 1f)
            }
        })
    }

//    private fun setupRecyclerView() {
//        playlistAdapterPlayer = BottomSheetPlaylistAdapter { playlist ->
//            val track = viewModel.trackInfo.value ?: return@BottomSheetPlaylistAdapter
//            viewModel.addTrackToPlaylist(track, playlist.id)
//        }
//
//
//        bottomSheetBinding.rvPlaylists.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = playlistAdapter
//        }
//    }


    private fun setupTrackInfo(trackData: Track?) {
        Glide.with(requireContext())
            .load(trackData?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")).transform(
                RoundedCorners(
                    requireContext().resources
                        .getDimensionPixelSize(R.dimen.album_image_corner_radius)
                )
            )
            .placeholder(R.drawable.album_placeholder)
            .into(binding.albumImage)

        binding.trackName.text = trackData?.trackName
        binding.artistName.text = trackData?.artistName
        binding.trackTime.text = trackData?.trackTime
        binding.releaseDate.text = trackData?.releaseDate?.substring(0, 4)
        binding.genreName.text = trackData?.primaryGenreName
        binding.country.text = trackData?.country
        if (trackData?.collectionName.isNullOrEmpty()) {
            binding.collectionNameGroup.visibility = View.GONE
        } else {
            binding.collectionName.text = trackData?.collectionName
            binding.collectionNameGroup.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

}