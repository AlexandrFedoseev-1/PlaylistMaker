package com.example.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.Creator
import com.example.playlistmaker.ui.audio_player.AudioPlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Track

class SearchActivity : AppCompatActivity() {
    private var savedText: String? = null

    private val trackList = ArrayList<Track>()

    private lateinit var tracksInteractor : TracksInteractor
    private lateinit var searchAdapter: SearchAdapter

    private lateinit var trackSearchHistoryList: ArrayList<Track>


    private lateinit var historyAdapter: SearchAdapter


    private lateinit var binding: ActivitySearchBinding

    private val debouncer = Debouncer(
        Handler(Looper.getMainLooper())
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tracksInteractor = Creator.provideTracksInteractor()

        searchAdapter = setupAdapter(trackList)
        binding.searchList.layoutManager = LinearLayoutManager(this)
        binding.searchList.adapter = searchAdapter



        trackSearchHistoryList = tracksInteractor.getSearchHistory()
        historyAdapter = setupAdapter(trackSearchHistoryList)

        binding.historySearchList.layoutManager = LinearLayoutManager(this)
        binding.historySearchList.adapter = historyAdapter

        binding.search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trackSearch()
                true
            }
            false
        }

        binding.search.setOnFocusChangeListener { _, _ ->
            searchHistoryLayoutVisibility(binding.search.text)
        }



        binding.clearSearchHistory.setOnClickListener {
            tracksInteractor.clearHistory()
            updateSearchHistory()
            setScreenState(ScreenState.EMPTY)
        }

        binding.backButton.setOnClickListener {
            super.finish()
        }

        binding.updateButton.setOnClickListener {
            trackSearch()
        }

        binding.clearText.setOnClickListener {
            binding.search.setText("")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(binding.search.windowToken, 0)
            trackList.clear()
            searchAdapter.notifyDataSetChanged()
            searchHistoryLayoutVisibility("")
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearText.visibility = clearButtonVisibility(s)
                searchHistoryLayoutVisibility(s)
                debouncer.submit { trackSearch() }
            }

            override fun afterTextChanged(s: Editable?) {
                savedText = s?.toString()
            }

        }

        binding.search.addTextChangedListener(searchTextWatcher)


    }
    private fun updateSearchHistory(){
        trackSearchHistoryList.clear()
        trackSearchHistoryList.addAll(tracksInteractor.getSearchHistory())
        historyAdapter.notifyDataSetChanged()
    }

    private fun setupAdapter(list: ArrayList<Track>): SearchAdapter {
        return SearchAdapter(list) { track ->
            if (debouncer.clickDebounce()) {
                tracksInteractor.addTrackToHistory(track)
                updateSearchHistory()
                val i = Intent(this, AudioPlayerActivity::class.java)
                i.putExtra(TRACK, track)
                startActivity(i)
            }
        }
    }


    private fun trackSearch() {
        if (binding.search.text.isNotEmpty()) {
            setScreenState(ScreenState.LOADING)
            debouncer.submit {
                tracksInteractor.searchTracks(
                    binding.search.text.toString(),
                    object : Consumer<List<Track>> {
                        override fun consume(data: ConsumerData<List<Track>>) {
                            runOnUiThread {
                                when (data) {
                                    is ConsumerData.Data -> {
                                        if (data.value.isNotEmpty()) {
                                            trackList.clear()
                                            trackList.addAll(data.value)
                                            searchAdapter.notifyDataSetChanged()
                                            showMessage(FOUND_SUCCESS)
                                        } else {
                                            showMessage(NOT_FOUND)
                                        }
                                    }

                                    is ConsumerData.Error -> {
                                        showMessage(NO_INTERNET)
                                    }
                                }
                            }
                        }

                    }
                )

            }
        } else {
            debouncer.cancel()
        }
    }

    private fun showMessage(massage: String) {
        if (massage == FOUND_SUCCESS) {
            setScreenState(ScreenState.SEARCH_LIST)
        } else {
            setScreenState(ScreenState.PLACEHOLDER)
            trackList.clear()
            searchAdapter.notifyDataSetChanged()
            if (massage == NOT_FOUND) {
                binding.placeholderImage.setImageResource(R.drawable.not_found)
                binding.placeholderText.text = getString(R.string.placeholder_not_found)
                binding.updateButton.isVisible = false
            } else {
                binding.placeholderImage.setImageResource(R.drawable.search_error)
                binding.placeholderText.text = getString(R.string.placeholder_error)
                binding.updateButton.isVisible = true
            }

        }
    }

    companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
        const val NOT_FOUND = "NOT_FOUND"
        const val NO_INTERNET = "NO_INTERNET"
        const val FOUND_SUCCESS = "FOUND_SUCCESS"
        const val TRACK = "TRACK"

    }

    private fun setScreenState(state: ScreenState) {
        binding.searchHistory.isVisible = state == ScreenState.SEARCH_HISTORY
        binding.searchList.isVisible = state == ScreenState.SEARCH_LIST
        binding.placeholder.isVisible = state == ScreenState.PLACEHOLDER
        binding.progressBar.isVisible = state == ScreenState.LOADING
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT, savedText)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedText = savedInstanceState.getString("savedText")
        findViewById<EditText>(R.id.search).setText(savedText)

    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchHistoryLayoutVisibility(s: CharSequence?) {
        if (binding.search.hasFocus() && s.isNullOrEmpty() && trackSearchHistoryList.isNotEmpty()) {
            trackList.clear()
            searchAdapter.notifyDataSetChanged()
            setScreenState(ScreenState.SEARCH_HISTORY)

        } else {
            setScreenState(ScreenState.EMPTY)
        }
    }

}