package com.example.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.consumer.Consumer
import com.example.playlistmaker.search.domain.consumer.ConsumerData
import com.example.playlistmaker.search.domain.models.Track

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {

    private val searchScreenStateLiveData = MutableLiveData<ScreenState>()
    val screenState: LiveData<ScreenState> get() = searchScreenStateLiveData

    private var latestSearchText: String? = null
    private lateinit var history: ArrayList<Track>
    private val debouncer = Debouncer(Handler(Looper.getMainLooper()))


    private var isCanceled = false
    init {
        loadSearchHistory()
        setEmptyState()
    }


    fun clearHistory() {
        tracksInteractor.clearHistory()
        history.clear()
        showHistory()
    }


    fun trackSearchDebounce(changedText: String) {
        if (latestSearchText == changedText && searchScreenStateLiveData.value !is ScreenState.Placeholder) {
            return
        }
        this.latestSearchText = changedText
        debouncer.submit { trackSearch(changedText) }
    }
    private fun cancelSearchDebounce(){
        isCanceled = true
        debouncer.cancel()
    }


    fun trackSearch(query: String) {
        if (query.isNotEmpty()) {
            isCanceled = false
            searchScreenStateLiveData.postValue(ScreenState.Loading)
            tracksInteractor.searchTracks(query, object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    if (!isCanceled) {
                        handleSearchResult(data)
                    }
                }
            })
        }
    }


    private fun handleSearchResult(data: ConsumerData<List<Track>>) {
        when (data) {
            is ConsumerData.Data -> handleSearchSuccess(data.value)
            is ConsumerData.Error -> searchScreenStateLiveData.postValue(
                ScreenState.Placeholder(
                    ScreenState.NO_INTERNET
                )
            )
        }
    }

    private fun handleSearchSuccess(tracks: List<Track>) {
        if (tracks.isNotEmpty()) {
            searchScreenStateLiveData.postValue(ScreenState.SearchResults(tracks))
        } else {
            searchScreenStateLiveData.postValue(ScreenState.Placeholder(ScreenState.NOT_FOUND))
        }
    }

    fun showHistory() {
        cancelSearchDebounce()
        searchScreenStateLiveData.value = ScreenState.SearchResults(emptyList())
        searchScreenStateLiveData.value = ScreenState.SearchHistory(history)
        if (history.isEmpty()) {
            searchScreenStateLiveData.value = ScreenState.Empty
        }
    }

    fun setEmptyState() {
        cancelSearchDebounce()
        searchScreenStateLiveData.value = ScreenState.Empty
    }

    private fun loadSearchHistory() {
        history = tracksInteractor.getSearchHistory()
    }

    fun addToSearchHistory(track: Track) {
        tracksInteractor.addTrackToHistory(track)
        loadSearchHistory()
        if (searchScreenStateLiveData.value is ScreenState.SearchHistory) {
            searchScreenStateLiveData.value = ScreenState.SearchHistory(history)
        }
    }

}