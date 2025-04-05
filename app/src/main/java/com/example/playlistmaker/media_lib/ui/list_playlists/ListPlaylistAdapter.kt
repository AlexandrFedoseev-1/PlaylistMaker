package com.example.playlistmaker.media_lib.ui.list_playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.DiffUtilUpdate
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.media_lib.domain.model.Playlist


class ListPlaylistAdapter(
    private val playlists: MutableList<Playlist> = mutableListOf(),
    private val onTrackClick: (Playlist) -> Unit
) :
    RecyclerView.Adapter<ListPlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPlaylistViewHolder {
        val binding =
            PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListPlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onTrackClick(playlists[position])
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun updateData(newPlaylists: List<Playlist>) {
        DiffUtilUpdate(
            oldList = playlists,
            newList = newPlaylists,
            adapter = this,
            areItemsTheSame = { old, new -> old.playlistId == new.playlistId },
            areContentsTheSame = { old, new -> old == new },
            updateData = {
                playlists.clear()
                playlists.addAll(it)
            }
        ).dispatch()
    }
}