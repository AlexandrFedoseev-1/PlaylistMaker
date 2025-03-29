package com.example.playlistmaker.media_lib.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.api.FavoriteTracksInteractor
import kotlinx.coroutines.launch

class FavoritesTracksViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {
    private val _favoriteState = MutableLiveData<FavoriteState>(FavoriteState.Placeholder)
    val favoriteState: LiveData<FavoriteState> get() = _favoriteState
    init {

        getFavoriteTracks()
    }

    private fun getFavoriteTracks(){
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