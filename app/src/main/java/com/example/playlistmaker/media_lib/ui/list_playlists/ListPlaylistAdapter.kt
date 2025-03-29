package com.example.playlistmaker.media_lib.ui.list_playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.media_lib.domain.model.Playlist

class ListPlaylistAdapter(private val playlists: MutableList<Playlist> = mutableListOf()) :
    RecyclerView.Adapter<ListPlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPlaylistViewHolder {
        val binding =
            PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListPlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
    fun updateData(newPlaylists: List<Playlist>) {
        playlists.clear()
        playlists.addAll(newPlaylists)
        notifyDataSetChanged()
    }
}