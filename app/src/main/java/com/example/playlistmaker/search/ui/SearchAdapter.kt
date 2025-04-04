package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.DiffUtilUpdate
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track

class SearchAdapter(
    private val tracks: MutableList<Track> = mutableListOf(),
    private val onTrackClick: (Track) -> Unit
) : RecyclerView.Adapter<SearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_track_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onTrackClick(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun updateData(newTracks: List<Track>) {
        DiffUtilUpdate(
            oldList = tracks,
            newList = newTracks,
            adapter = this,
            areItemsTheSame = { old, new -> old.trackId == new.trackId },
            areContentsTheSame = { old, new -> old == new },
            updateData = {
                tracks.clear()
                tracks.addAll(it)
            }
        ).dispatch()
    }
}