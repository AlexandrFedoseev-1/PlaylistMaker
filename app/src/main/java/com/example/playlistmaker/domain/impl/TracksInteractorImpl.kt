package com.example.playlistmaker.domain.impl


import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    private val executor= Executors.newCachedThreadPool()
    override fun searchTracks(exception: String, consumer: Consumer<List<Track>>) {
        executor.execute{
            when (val tracksResponse =
                repository.getSearchTracks(exception)) {
                is Resource.Success -> {
                    consumer.consume(ConsumerData.Data(tracksResponse.data))
                }
                is Resource.Error -> {
                    consumer.consume(ConsumerData.Error(tracksResponse.message))
                }
            }
        }
    }

    override fun getSearchHistory(): ArrayList<Track> {
        return repository.getSearchHistory()
    }

    override fun addTrackToHistory(track: Track) {
        repository.addTrackToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}