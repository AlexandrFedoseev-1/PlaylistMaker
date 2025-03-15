package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val searchScreenStateLiveData = MutableLiveData<ScreenState>()
    val screenState: LiveData<ScreenState> get() = searchScreenStateLiveData

    private var latestSearchText: String? = null
    private lateinit var history: ArrayList<Track>

    private var searchJob: Job? = null

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
        latestSearchText = changedText
        searchJob?.cancel() // Отменяем предыдущее выполнение
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            if (isActive) {
                trackSearch(changedText)
            }
        }
    }

    private fun cancelSearchDebounce() {
        searchJob?.cancel() // Отмена дебаунс-таймера
    }

    fun trackSearch(query: String) {
        if (query.isNotEmpty()) {
            searchScreenStateLiveData.postValue(ScreenState.Loading)

            searchJob?.cancel() // Отменяем предыдущий поиск, если он был
            searchJob = viewModelScope.launch {
                tracksInteractor.searchTracks(query).collect { pair ->
                    if (isActive) { // Проверяем, не отменили ли корутину
                        handleSearchResult(pair.first, pair.second)
                    }
                }
            }
        }
    }

    private  fun handleSearchResult(tracks: List<Track>?, errorMessage: String?) {
        when {
            tracks.isNullOrEmpty() -> searchScreenStateLiveData.postValue(
                ScreenState.Placeholder(ScreenState.NOT_FOUND)
            )

            errorMessage != null -> searchScreenStateLiveData.postValue(
                ScreenState.Placeholder(ScreenState.NO_INTERNET)
            )

            else -> searchScreenStateLiveData.postValue(ScreenState.SearchResults(tracks))
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

    fun loadSearchHistory() {
        viewModelScope.launch {  tracksInteractor.getSearchHistory().collect{trackHistory ->
            history = ArrayList(trackHistory)
            if (searchScreenStateLiveData.value is ScreenState.SearchHistory) {
                searchScreenStateLiveData.value = ScreenState.SearchHistory(history)
            }
        } }

    }

    fun addToSearchHistory(track: Track) {
        tracksInteractor.addTrackToHistory(track)
        loadSearchHistory()
    }
}