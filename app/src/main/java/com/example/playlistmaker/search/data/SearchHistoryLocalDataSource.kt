package com.example.playlistmaker.search.data


import android.content.SharedPreferences
import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.search.domain.models.Track

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class SearchHistoryLocalDataSource(
    private val sharedPref: SharedPreferences,
    private val appDatabase: AppDatabase,
    private val gson: Gson
) {


    fun getSearchHistory(): Flow<List<Track>> = flow {
        val json: String =
            sharedPref.getString(SEARCH_HISTORY_KEY, null) ?: return@flow emit(emptyList())
        val favoriteIds = appDatabase.favoriteTrackDao().getTracksId()
        val type = object : TypeToken<List<Track>>() {}.type
        val trackList = gson.fromJson<List<Track>>(json, type)

        emit(trackList.map { track ->
            track.copy(isFavorite = track.trackId in favoriteIds)
        })
    }

    private fun getSearchHistorySync(): List<Track> {
        val json: String = sharedPref.getString(SEARCH_HISTORY_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun addTrackToHistory(track: Track) {
        val history = getSearchHistorySync().toMutableList()
        history.removeAll { it.trackId == track.trackId }
        history.add(0, track)
        if (history.size > MAX_HISTORY_SIZE) {
            history.removeAt(history.size - 1)
        }
        saveSearchHistory(history)
    }

    fun clearHistory() {
        sharedPref.edit().remove(SEARCH_HISTORY_KEY).apply()
    }

    private fun saveSearchHistory(history: List<Track>) {
        val json = Gson().toJson(history)
        sharedPref.edit().putString(SEARCH_HISTORY_KEY, json).apply()
    }

    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history_key"
        private const val MAX_HISTORY_SIZE = 10
        const val SEARCH_HISTORY_PREFERENCES = "search_history"
    }
}