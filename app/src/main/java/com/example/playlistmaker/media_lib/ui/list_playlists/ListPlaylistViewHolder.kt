package com.example.playlistmaker.media_lib.ui.list_playlists

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.media_lib.domain.model.Playlist


class ListPlaylistViewHolder(private val binding: PlaylistItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(playlist: Playlist) {
        binding.playlistName.text = playlist.name
        binding.playListCount.text = "${playlist.tracksCount} треков"

        val imagePath = playlist.imageUri

        Glide.with(itemView)
            .load(imagePath)
            .placeholder(R.drawable.album_placeholder)
            .into(binding.playlistCoverImage)
    }
}