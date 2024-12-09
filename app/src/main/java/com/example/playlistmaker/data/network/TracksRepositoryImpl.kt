package com.example.playlistmaker.data.network

import android.icu.text.SimpleDateFormat
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.SearchHistoryManager
import com.example.playlistmaker.data.dto.TrackRequest
import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track
import java.util.Locale

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchHistoryManager: SearchHistoryManager
) : TracksRepository {
    override fun getSearchTracks(exception: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackRequest(exception))
        if (response.resultCode == 200) {
            val respList = (response as TrackResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTime.toInt()),
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
            return Resource.Success(respList)
        } else {
            return Resource.Error("Произошла сетевая ошибка")
        }
    }

    override fun getSearchHistory(): ArrayList<Track> {
        return searchHistoryManager.getSearchHistory()
    }

    override fun addTrackToHistory(track: Track) {
        searchHistoryManager.addTrackToHistory(track)
    }

    override fun clearHistory() {
        searchHistoryManager.clearHistory()
    }
}