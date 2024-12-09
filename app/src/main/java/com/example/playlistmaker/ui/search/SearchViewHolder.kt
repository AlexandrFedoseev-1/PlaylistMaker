package com.example.playlistmaker.ui.search


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackImage: ImageView = itemView.findViewById(R.id.track_image)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)

    fun bind(track: Track) {
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(itemView.resources
                .getDimensionPixelSize(R.dimen.track_image_corner_radius)))
            .placeholder(R.drawable.placeholder)
            .into(trackImage)
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTime
    }

}