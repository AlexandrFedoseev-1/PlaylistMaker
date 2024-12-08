package com.example.playlistmaker.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.domain.models.Track

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchHistoryManager(context: Context) {
    private val sharedPref = context.getSharedPreferences(SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE)

     fun getSearchHistory(): ArrayList<Track> {
        val json = sharedPref.getString(SEARCH_HISTORY_KEY, null)
        if (json != null) {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            return Gson().fromJson(json, type)
        }
        return ArrayList()
    }



    fun addTrackToHistory(track: Track) {
        val history = getSearchHistory().toMutableList()
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