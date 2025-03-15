package com.example.playlistmaker.media_lib.ui.fragments.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.api.FavoriteTracksInteractor
import com.example.playlistmaker.media_lib.ui.fragments.FavoriteState
import kotlinx.coroutines.launch

class FavoritesTracksViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {
    private val _favoriteState = MutableLiveData<FavoriteState>(FavoriteState.Placeholder)
    val favoriteState: LiveData<FavoriteState> get() = _favoriteState
    init {

        getFavoriteTracks()
    }

    fun getFavoriteTracks(){
        viewModelScope.launch {
             favoriteTracksInteractor.getTracks().collect{ tracks ->
                 if (tracks.isEmpty()){
                     _favoriteState.postValue(FavoriteState.Placeholder)
                 }else {
                      _favoriteState.postValue(FavoriteState.TrackList(tracks))
                 }
            }
        }

    }
}