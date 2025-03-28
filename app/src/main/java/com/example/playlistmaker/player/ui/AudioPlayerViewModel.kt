package com.example.playlistmaker.player.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.api.FavoriteTracksInteractor
import com.example.playlistmaker.db.domain.api.PlaylistInteractor
import com.example.playlistmaker.media_lib.domain.model.Playlist
import com.example.playlistmaker.player.AddPlaylistResult
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AudioPlayerViewModel(
    private val player: AudioPlayerInteractor,
    private val favoriteTracks: FavoriteTracksInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private var timerJob: Job? = null
    private var previewUrl: String = ""
    private var trackId: String = ""


    private val _playerState =
        MutableLiveData<AudioPlayerScreenState>(AudioPlayerScreenState.Default())
    val playerState: LiveData<AudioPlayerScreenState> get() = _playerState

    private val _isFavoriteLiveData = MutableLiveData<Boolean>()
    val isFavoriteLiveData: LiveData<Boolean> get() = _isFavoriteLiveData

    private val _playlists = MutableLiveData<List<Playlist>>(mutableListOf())
    val playlists: LiveData<List<Playlist>> get() = _playlists

    private val _addTrackStatus = MutableLiveData<AddPlaylistResult>()
    val addTrackStatus: LiveData<AddPlaylistResult> get() = _addTrackStatus

    init {
        getPlaylists()
    }

    private fun getPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylist().collect { listPlaylist ->
                _playlists.postValue(listPlaylist)
            }
        }
    }

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        if (playlist.tracksId.contains(trackId)) {
            _addTrackStatus.postValue(AddPlaylistResult.Error(playlist.name))
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                if (playlistInteractor.addTrackToPlaylist(track, playlist))
                    _addTrackStatus.postValue(AddPlaylistResult.Success(playlist.name))
            }
        }
    }

    fun setValues(trackId: String, previewUrl: String) {
        this.previewUrl = previewUrl
        this.trackId = trackId
        checkFavorites()
        playerPrepare()
    }

    private fun checkFavorites() {
        viewModelScope.launch {
            val isFavorite = favoriteTracks.getTracksId().contains(trackId)
            _isFavoriteLiveData.postValue(isFavorite)
        }
    }


    fun onFavoriteClicked(track: Track?) {
        if (track != null) {
            viewModelScope.launch {
                if (_isFavoriteLiveData.value!!) {
                    favoriteTracks.deleteFromFavorite(track)
                } else {
                    favoriteTracks.addToFavorite(track)
                }
                _isFavoriteLiveData.value = !_isFavoriteLiveData.value!!
            }
        }
    }

    private fun playerPrepare() {
        player.prepare(previewUrl,
            onPrepared = {
                _playerState.value = AudioPlayerScreenState.Prepared()
            },
            onCompletion = {
                timerJob?.cancel()
                _playerState.value = AudioPlayerScreenState.Prepared()
            }
        )
    }


    private fun startPlayer() {
        player.start()
        _playerState.value = AudioPlayerScreenState.Playing(player.getCurrentPosition())
        startTimer()
    }

    fun pausePlayer() {
        player.pause()
        timerJob?.cancel()
        _playerState.value = AudioPlayerScreenState.Paused(player.getCurrentPosition())
    }


    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (player.isPlaying) {
                delay(DELAY)
                _playerState.value = AudioPlayerScreenState.Playing(player.getCurrentPosition())
            }
        }
    }

    fun playbackControl() {
        when (playerState.value) {
            is AudioPlayerScreenState.Playing -> pausePlayer()
            is AudioPlayerScreenState.Prepared, is AudioPlayerScreenState.Paused -> startPlayer()
            else -> Unit
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.stop()
        player.release()
    }

    companion object {
        const val DELAY = 300L
    }
}