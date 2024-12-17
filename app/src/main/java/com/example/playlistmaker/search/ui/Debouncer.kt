package com.example.playlistmaker.search.ui

import android.os.Handler

class Debouncer(private val handler: Handler) {
    private var runnable: Runnable? = null
    private var isClickAllowed = true
    fun submit(task: () -> Unit) {
        runnable?.let { handler.removeCallbacks(it) }
        runnable = Runnable { task() }
        handler.postDelayed(runnable!!, SUBMIT_DEBOUNCE_DELAY)
    }

    fun cancel() {
        runnable?.let { handler.removeCallbacks(it) }
        runnable = null
    }

    fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SUBMIT_DEBOUNCE_DELAY = 2000L
    }
}