package com.example.playlistmaker.search.data.network

import android.icu.text.SimpleDateFormat
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.SearchHistoryLocalDataSource
import com.example.playlistmaker.search.data.dto.TrackRequest
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Resource
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Locale

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchHistoryLocalDataSource: SearchHistoryLocalDataSource
) : TracksRepository {
    override fun getSearchTracks(exception: String): Flow<Resource<List<Track>>> = flow {
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
            emit(Resource.Success(respList))
        } else {
            emit(Resource.Error("Произошла сетевая ошибка"))
        }
    }

    override fun getSearchHistory(): ArrayList<Track> {
        return searchHistoryLocalDataSource.getSearchHistory()
    }

    override fun addTrackToHistory(track: Track) {
        searchHistoryLocalDataSource.addTrackToHistory(track)
    }

    override fun clearHistory() {
        searchHistoryLocalDataSource.clearHistory()
    }
}