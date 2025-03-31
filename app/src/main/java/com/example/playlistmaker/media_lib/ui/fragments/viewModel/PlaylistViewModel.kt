package com.example.playlistmaker.media_lib.ui.fragments.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.api.PlaylistInteractor
import com.example.playlistmaker.media_lib.domain.model.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val _playlists = MutableLiveData<List<Playlist>>(mutableListOf())
    val playlists: LiveData<List<Playlist>> get() = _playlists

    init {
        getPlaylists()
    }
    private fun getPlaylists(){
        viewModelScope.launch {
            playlistInteractor.getPlaylist().collect{ listPlaylist ->
                _playlists.postValue(listPlaylist)
            }
        }
    }
}