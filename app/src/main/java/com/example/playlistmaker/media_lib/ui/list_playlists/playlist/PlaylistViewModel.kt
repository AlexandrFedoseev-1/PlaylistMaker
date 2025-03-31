package com.example.playlistmaker.media_lib.ui.list_playlists.playlist

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.api.PlaylistInteractor
import com.example.playlistmaker.media_lib.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {
    private val _tracksFromPlaylist = MutableLiveData<List<Track>>(emptyList())
    val tracksFromPlaylist: LiveData<List<Track>> get() = _tracksFromPlaylist
    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> get() = _playlist


    fun getPlaylist(id: Int) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(id).collect {
                getTracks(it.tracksId)
                _playlist.postValue(it)

            }
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            withContext(NonCancellable) {
                playlistInteractor.deletePlaylist(playlist)
            }
        }
    }

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            playlist.value?.let { currentPlaylist ->
                playlistInteractor.deleteTrackFromPlaylist(track.trackId, currentPlaylist)
                getTracks(currentPlaylist.tracksId.filter { it != track.trackId })
            }
        }
    }


    private fun getTracks(tracksId: List<String>) {
        viewModelScope.launch {
            playlistInteractor.getTracksFromPlaylist(tracksId).collect { tracks ->
                _tracksFromPlaylist.postValue(tracks)
            }
        }
    }

    fun getAllTracksTime(): String {
        val tracks = _tracksFromPlaylist.value ?: emptyList()
        val allTracksTime = tracks.sumOf { convertTimeToMillis(it.trackTime) }
        return SimpleDateFormat("mm", Locale.getDefault()).format(Date(allTracksTime))
    }

    fun sharePlaylist(context: Context) {
        val shareText = createShareMessage()
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(shareIntent, "Поделиться плейлистом"))
    }

    private fun createShareMessage(): String {
        val playlist = _playlist.value!!
        val tracks = _tracksFromPlaylist.value!!
        val builder = StringBuilder()

        builder.append(playlist.name).append("\n")

        builder.append(playlist.description).append("\n")

        builder.append(String.format(Locale.getDefault(), "%d треков", playlist.tracksCount))
            .append("\n\n")

        tracks.forEachIndexed { index, track ->
            builder.append("${index + 1}. ${track.artistName} - ${track.trackName} (${track.trackTime})")
            if (index < tracks.lastIndex) {
                builder.append("\n")
            }
        }
        return builder.toString()
    }

    private fun convertTimeToMillis(time: String): Long {
        val timeParts = time.split(":")
        val minutes = timeParts[0].toLong()
        val seconds = timeParts[1].toLong()
        return (minutes * 60 + seconds) * 1000
    }

}