package com.example.playlistmaker.media_lib.ui.edit_playlist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.media_lib.domain.model.Playlist
import com.example.playlistmaker.media_lib.ui.add_playlist.AddPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : AddPlaylistFragment() {

    private val args: EditPlaylistFragmentArgs by navArgs()
    override val viewModel by viewModel<EditPlaylistViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Настройка toolbar для редактирования
        binding.toolbar.title = getString(R.string.edit_playlist)
        binding.createButton.text = getString(R.string.save_playlist)
        binding.toolbar.setNavigationOnClickListener { backClick() }

        // Заполнение полей данными переданного плейлиста
        val playlist = args.playlist
        binding.playlistName.setText(playlist.name)
        binding.playlistDescription.setText(playlist.description)
        Glide.with(view)
            .load(playlist.imageUri)
            .placeholder(R.drawable.album_placeholder)
            .into(binding.addImagePlaylist)
        coverImagePath = playlist.imageUri

        setupCommonUI()

        binding.createButton.setOnClickListener { saveEditedPlaylist(playlist) }
    }

    private fun saveEditedPlaylist(originalPlaylist: Playlist) {
        val name = binding.playlistName.text?.toString()?.trim()
        val description = binding.playlistDescription.text?.toString()?.trim() ?: ""

        if (name.isNullOrEmpty()) {
            Toast.makeText(context, "Введите название плейлиста", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedPlaylist = originalPlaylist.copy(
            name = name,
            description = description,
            imageUri = coverImagePath ?: originalPlaylist.imageUri
            // tracksId и tracksCount остаются без изменений
        )
        viewModel.updatePlaylist(updatedPlaylist)
        showSnackBar("Плейлист \"$name\" сохранён")
        findNavController().navigateUp()
    }

    // При нажатии "Назад" просто возвращаемся без сохранения
    override fun backClick() {
        findNavController().navigateUp()
    }
}