package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private var savedText: String? = null
    private val trackBaseUrl = BASE_URL
    private val retrofit =
        Retrofit.Builder().baseUrl(trackBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()
    private val trackService = retrofit.create(TrackSearchApi::class.java)
    private val trackList = ArrayList<Track>()


    private lateinit var searchAdapter: SearchAdapter

    private lateinit var trackSearchHistoryList: ArrayList<Track>
    private lateinit var sharedPref: SharedPreferences
    private lateinit var searchHistoryManager: SearchHistoryManager
    private lateinit var historyAdapter: SearchAdapter
    private lateinit var sharedPrefChangeListener: SharedPreferences.OnSharedPreferenceChangeListener

    private lateinit var binding: ActivitySearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences(SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE)
        searchHistoryManager = SearchHistoryManager(sharedPref)


        searchAdapter =
            SearchAdapter(trackList) { track ->
                searchHistoryManager.addTrackToHistory(track)
                val i = Intent(this,AudioPlayerActivity::class.java)
                i.putExtra(TRACK,track)
                startActivity(i)
            }
        binding.searchList.layoutManager = LinearLayoutManager(this)
        binding.searchList.adapter = searchAdapter



        trackSearchHistoryList = searchHistoryManager.getSearchHistory()
        historyAdapter =
            SearchAdapter(trackSearchHistoryList) { track ->
                searchHistoryManager.addTrackToHistory(track)
                val i = Intent(this,AudioPlayerActivity::class.java)
                i.putExtra(TRACK,track)
                startActivity(i)
            }
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
            binding.searchHistory.visibility = searchHistoryLayoutVisibility(binding.search.text)
        }


        sharedPrefChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                trackSearchHistoryList.clear()
                trackSearchHistoryList.addAll(searchHistoryManager.getSearchHistory())
                historyAdapter.notifyDataSetChanged()
            }
        sharedPref.registerOnSharedPreferenceChangeListener(sharedPrefChangeListener)


        binding.clearText.setOnClickListener {
            searchHistoryManager.clearHistory()
            binding.searchHistory.visibility = View.GONE
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
            binding.placeholder.visibility = View.GONE
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearText.visibility = clearButtonVisibility(s)
                binding.searchHistory.visibility = searchHistoryLayoutVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                savedText = s?.toString()
            }

        }

        binding.search.addTextChangedListener(searchTextWatcher)


    }

    private fun trackSearch() {
        if (binding.search.text.isNotEmpty()) {
            trackService.search(binding.search.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>, response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            trackList.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.addAll(response.body()?.results!!)
                                searchAdapter.notifyDataSetChanged()
                            }
                            if (trackList.isEmpty()) {
                                showMessage(
                                    getString(R.string.placeholder_not_found), response.code()
                                )
                            } else {
                                showMessage("", response.code())
                            }
                        } else {
                            showMessage(
                                getString(R.string.placeholder_error), response.code()
                            )
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showMessage(getString(R.string.placeholder_error), null)
                    }
                })
        }
    }

    private fun showMessage(text: String, code: Int?) {
        if (text.isNotEmpty()) {
            binding.placeholder.visibility = View.VISIBLE
            trackList.clear()
            searchAdapter.notifyDataSetChanged()
            binding.placeholderText.text = text
            if (code == 200) {
                binding.placeholderImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this, R.drawable.not_found
                    )
                )
                binding.updateButton.visibility = View.GONE
            } else {
                binding.placeholderImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this, R.drawable.search_error
                    )
                )
                binding.updateButton.visibility = View.VISIBLE
            }
        } else {
            binding.placeholder.visibility = View.GONE
        }
    }

    companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
        const val BASE_URL = "https://itunes.apple.com"
        const val SEARCH_HISTORY_PREFERENCES = "search_history"
        const val TRACK = "TRACK"

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

    private fun searchHistoryLayoutVisibility(s: CharSequence?): Int {
        if (binding.search.hasFocus() && s.isNullOrEmpty() && trackSearchHistoryList.isNotEmpty()) {
            binding.placeholder.visibility = View.GONE
            return View.VISIBLE
        } else {
            return View.GONE
        }
    }

}