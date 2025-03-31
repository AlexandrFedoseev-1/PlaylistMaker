package com.example.playlistmaker.media_lib.ui.edit_playlist

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.api.PlaylistInteractor
import com.example.playlistmaker.media_lib.domain.model.Playlist
import com.example.playlistmaker.media_lib.ui.add_playlist.AddPlaylistViewModel
import kotlinx.coroutines.launch

class EditPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) :
    AddPlaylistViewModel(playlistInteractor) {
        fun updatePlaylist(playlist: Playlist){
            viewModelScope.launch { playlistInteractor.updatePlaylist(playlist) }
        }
}