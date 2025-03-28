package com.example.playlistmaker.player

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.BottomSheetItemBinding

import com.example.playlistmaker.media_lib.domain.model.Playlist

class BottomSheetPlaylistViewHolder(private val binding: BottomSheetItemBinding) :
RecyclerView.ViewHolder(binding.root) {
    fun bind(playlist: Playlist) {
        binding.playlistName.text = playlist.name
        binding.tracksCount.text = "${playlist.tracksCount} треков"

        val imagePath = playlist.imageUri

        Glide.with(itemView)
            .load(imagePath)
            .transform(RoundedCorners(itemView.resources
                .getDimensionPixelSize(R.dimen.track_image_corner_radius)))
            .placeholder(R.drawable.album_placeholder)
            .into(binding.playlistImage)
    }
}