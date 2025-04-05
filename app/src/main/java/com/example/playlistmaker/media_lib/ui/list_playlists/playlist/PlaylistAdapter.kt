package com.example.playlistmaker.media_lib.ui.list_playlists.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.DiffUtilUpdate
import com.example.playlistmaker.databinding.FavoriteTrackItemBinding
import com.example.playlistmaker.media_lib.ui.favorites.FavoriteViewHolder
import com.example.playlistmaker.search.domain.models.Track

class PlaylistAdapter(
    private val tracks: MutableList<Track> = mutableListOf(),
    private val onTrackClick: (Track) -> Unit,
    private val onLongTrackClick: (Track) -> Unit
) : RecyclerView.Adapter<FavoriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return FavoriteViewHolder(FavoriteTrackItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val track = tracks.getOrNull(position) ?: return
        holder.bind(track)
        holder.itemView.setOnLongClickListener {
            onLongTrackClick(track)
            true
        }
        holder.itemView.setOnClickListener {
            onTrackClick(track)
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