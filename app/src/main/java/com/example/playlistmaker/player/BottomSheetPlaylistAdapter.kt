package com.example.playlistmaker.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.DiffUtilUpdate
import com.example.playlistmaker.databinding.BottomSheetItemBinding
import com.example.playlistmaker.media_lib.domain.model.Playlist


class BottomSheetPlaylistAdapter(
    private val playlists: MutableList<Playlist> = mutableListOf(),
    private val onPlaylistClick: (Playlist) -> Unit
) :
    RecyclerView.Adapter<BottomSheetPlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetPlaylistViewHolder {
        val binding =
            BottomSheetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BottomSheetPlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BottomSheetPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onPlaylistClick(playlists[position])
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