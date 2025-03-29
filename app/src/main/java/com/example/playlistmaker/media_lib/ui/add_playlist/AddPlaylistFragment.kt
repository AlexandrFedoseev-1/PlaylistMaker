package com.example.playlistmaker.media_lib.ui.add_playlist

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.example.playlistmaker.media_lib.domain.model.Playlist
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddPlaylistFragment : Fragment() {

    companion object {
        fun newInstance() = AddPlaylistFragment()
    }

    private lateinit var binding: FragmentAddPlaylistBinding
    private val viewModel by viewModel<AddPlaylistViewModel>()
    private var coverImagePath: String? = null
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            //обрабатываем событие выбора пользователем фотографии
            if (uri != null) {
                binding.addImagePlaylist.setImageURI(uri)
                coverImagePath = viewModel.saveImageToPrivateStorage(uri, requireActivity())
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener { backClick() }

        binding.addImagePlaylist.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.playlistName.addTextChangedListener {
            binding.createButton.isEnabled = !it.isNullOrEmpty()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            backClick()
        }

        binding.createButton.setOnClickListener {
            savePlaylist()
        }
    }

    private fun savePlaylist() {
        val name = binding.playlistName.text?.toString()?.trim()
        val description = binding.playlistDescription.text?.toString()?.trim() ?: ""

        if (name.isNullOrEmpty()) {
            Toast.makeText(context, "Введите название плейлиста", Toast.LENGTH_SHORT).show()
            return
        }

        val defaultImagePath =
            "android.resource://${requireContext().packageName}/${R.drawable.album_placeholder}"

        val playlist = Playlist(
            playlistId = 0,
            name = name,
            description = description,
            imageUri = coverImagePath ?: defaultImagePath,
            tracksId = emptyList(),
            tracksCount = 0
        )
        viewModel.createPlaylist(playlist)
        showSnackBar("Плейлист \"$name\" создан")
        findNavController().navigateUp()
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

    private fun confirmDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.playlist_cancel_request)
            .setMessage(R.string.unsaved_date_description)
            .setNeutralButton(R.string.cansel, null)
            .setPositiveButton(R.string.complete) { _, _ ->
                findNavController().navigateUp()
            }
            .show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            ?.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL)
            ?.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))

    }

    private fun isDataModified(): Boolean {
        return !binding.playlistName.text.isNullOrEmpty() ||
                !binding.playlistDescription.text.isNullOrEmpty() ||
                coverImagePath != null
    }

    private fun backClick() {
        if (isDataModified()) {
            confirmDialog()
        } else {
            findNavController().navigateUp()
        }
    }

}