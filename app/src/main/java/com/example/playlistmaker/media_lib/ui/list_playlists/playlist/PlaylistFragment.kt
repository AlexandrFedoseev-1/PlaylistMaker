package com.example.playlistmaker.media_lib.ui.list_playlists.playlist

import android.app.AlertDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.BottomSheetPlaylistBinding
import com.example.playlistmaker.databinding.BottomSheetPlaylistShareBinding
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.debounce
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class PlaylistFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistFragment()
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }

    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var bottomSheetPlaylistBinding: BottomSheetPlaylistBinding
    private lateinit var bottomSheetPlaylistShareBinding: BottomSheetPlaylistShareBinding
    private val args: PlaylistFragmentArgs by navArgs()
    private lateinit var bottomSheetBehaviorTracks: BottomSheetBehavior<View>
    private lateinit var bottomSheetBehaviorMore: BottomSheetBehavior<View>
    private val viewModel: PlaylistViewModel by viewModel()
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var onTrackLongClickDebounce: (Track) -> Unit

    private val playlistAdapter by lazy {
        PlaylistAdapter(
            onTrackClick = { track ->
                onTrackClickDebounce(track)
            },
            onLongTrackClick = { track ->
                onTrackLongClickDebounce(track)
            }
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        bottomSheetPlaylistBinding =
            BottomSheetPlaylistBinding.bind(binding.root.findViewById(R.id.bottom_sheet_playlist))
        bottomSheetPlaylistShareBinding =
            BottomSheetPlaylistShareBinding.bind(binding.root.findViewById(R.id.bottom_sheet_playlist_share))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistId = args.playlist.playlistId
        viewModel.getPlaylist(playlistId)

        viewModel.tracksFromPlaylist.observe(viewLifecycleOwner) { tracks ->
            playlistAdapter.updateData(tracks)
            viewModel.playlist.value?.let { playlist ->
                binding.sumTimePlaylistAndCount.text = getString(
                    R.string.all_time_and_count_playlist,
                    viewModel.getAllTracksTime(),
                    playlist.tracksCount.toString()
                )
                bottomSheetPlaylistBinding.emptyPlaylistPlaceholder.isVisible = tracks.isEmpty()
            }
        }
        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            Glide.with(view)
                .load(playlist.imageUri)
                .placeholder(R.drawable.album_placeholder)
                .into(binding.imagePlaylist)
            binding.playlistName.text = playlist.name
            binding.descriptionPlaylist.text = playlist.description

            Glide.with(view)
                .load(playlist.imageUri)
                .placeholder(R.drawable.album_placeholder)
                .into(bottomSheetPlaylistShareBinding.playlistImage)
            bottomSheetPlaylistShareBinding.playlistName.text = playlist.name
            bottomSheetPlaylistShareBinding.tracksCount.text = getString(R.string.tracks_count_playlist,playlist.tracksCount)
        }


        onTrackClickDebounce =
            debounce(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { track ->
                val action =
                    PlaylistFragmentDirections.actionPlaylistFragmentToAudioPlayerFragment(track)
                findNavController().navigate(action)
            }
        onTrackLongClickDebounce = { showDeleteTrackDialog(it) }
        bottomSheetPlaylistBinding.rvPlaylists.adapter = playlistAdapter
        setupBottomSheet()

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        binding.playlistShareButton.setOnClickListener { sharePlaylist() }

        bottomSheetPlaylistShareBinding.sharePlaylist.setOnClickListener { sharePlaylist() }

        bottomSheetPlaylistShareBinding.deletePlaylist.setOnClickListener { showDeletePlaylistDialog() }

        binding.playlistMoreButton.setOnClickListener { bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_COLLAPSED }

        bottomSheetPlaylistShareBinding.editPlaylist.setOnClickListener {
            val action = PlaylistFragmentDirections.actionPlaylistFragmentToEditPlaylistFragment(viewModel.playlist.value!!)
            findNavController().navigate(action)
        }
    }

    private fun showDeleteTrackDialog(track: Track) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить трек")
            .setMessage("Удалить трек из плейлиста?")
            .setNegativeButton("Нет") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Да") { _, _ -> viewModel.deleteTrackFromPlaylist(track) }
            .show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            ?.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            ?.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
    }

    private fun showDeletePlaylistDialog() {
        bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_HIDDEN
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить плейлист \"${viewModel.playlist.value?.name}\"?")
            .setMessage("Вы уверены, что хотите удалить этот плейлист?")
            .setNegativeButton("Нет") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Да") { _, _ ->
                viewModel.deletePlaylist(viewModel.playlist.value!!)
                findNavController().navigateUp()
            }
            .show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            ?.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            ?.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
    }

    private fun sharePlaylist() {
        val playlist = viewModel.playlist.value
        if (playlist == null || playlist.tracksId.isEmpty()) {
            Toast.makeText(requireContext(), R.string.emptyTrackSend, Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.sharePlaylist(requireContext())
    }

    private fun setupBottomSheet() {
        bottomSheetBehaviorTracks =
            BottomSheetBehavior.from(bottomSheetPlaylistBinding.root as View)

        bottomSheetBehaviorMore =
            BottomSheetBehavior.from(bottomSheetPlaylistShareBinding.root as View).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
                addBottomSheetCallback(object :
                    BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        binding.overlayPlaylistTrack.isVisible =  newState != BottomSheetBehavior.STATE_HIDDEN
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        binding.overlayPlaylistTrack.alpha =
                            ((slideOffset + 1) / 2).coerceIn(0f, 1f)
                    }
                })


            }
        binding.coordinatorLayout.post {
            val parentHeight = binding.coordinatorLayout.height
            val contentBottom = binding.constraintLayout.bottom
            val desiredPeekHeight = parentHeight - contentBottom
            bottomSheetBehaviorTracks.peekHeight = desiredPeekHeight
//            bottomSheetBehaviorMore.peekHeight = parentHeight/2
        }
    }
}