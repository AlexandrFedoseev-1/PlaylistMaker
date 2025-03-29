package com.example.playlistmaker.media_lib.ui.favorites


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FavoriteTrackItemBinding

import com.example.playlistmaker.search.domain.models.Track

class FavoriteViewHolder(private val binding: FavoriteTrackItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .transform(
                RoundedCorners(
                    itemView.resources
                        .getDimensionPixelSize(R.dimen.track_image_corner_radius)
                )
            )
            .placeholder(R.drawable.placeholder)
            .into(binding.trackImage)
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text = track.trackTime
    }

}