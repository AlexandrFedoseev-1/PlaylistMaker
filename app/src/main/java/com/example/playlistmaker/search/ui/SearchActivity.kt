package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.models.Track

class SearchActivity : AppCompatActivity() {
    private var savedText: String? = null
    private lateinit var viewModel: SearchViewModel

    private val searchAdapter by lazy { SearchAdapter(onTrackClick = ::onTrackClick) }
    private val historyAdapter by lazy { SearchAdapter(onTrackClick = ::onTrackClick) }


    private lateinit var binding: ActivitySearchBinding

    private val debouncer = Debouncer(
        Handler(Looper.getMainLooper())
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchList.adapter = searchAdapter
        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]


        binding.historySearchList.adapter = historyAdapter

        binding.search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.trackSearchDebounce(binding.search.text.toString())
                true
            }
            false
        }

        binding.search.setOnFocusChangeListener { _, _ ->
            searchHistoryLayoutVisibility(binding.search.text)
        }

        binding.clearSearchHistory.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.toolbar.setNavigationOnClickListener { super.finish() }

        binding.updateButton.setOnClickListener {
            viewModel.trackSearch(binding.search.text.toString())
        }

        binding.clearText.setOnClickListener {
            binding.search.setText("")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(binding.search.windowToken, 0)
            searchHistoryLayoutVisibility("")
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearText.isVisible = !s.isNullOrEmpty()
                searchHistoryLayoutVisibility(s?:"")
                viewModel.trackSearchDebounce(s?.toString()?:"")


            }

            override fun afterTextChanged(s: Editable?) {
                savedText = s?.toString()
            }

        }

        binding.search.addTextChangedListener(searchTextWatcher)


        viewModel.screenState.observe(this) { state ->
            when (state) {
                ScreenState.Empty -> setScreenState(state)
                ScreenState.Loading -> setScreenState(state)
                is ScreenState.Placeholder -> {
                    showMessage(state.massage)
                    setScreenState(state)
                }

                is ScreenState.SearchHistory -> {
                    historyAdapter.updateData(state.history)
                    setScreenState(state)
                }

                is ScreenState.SearchResults -> {
                    searchAdapter.updateData(state.results)
                    setScreenState(state)
                }
            }
        }
    }


    private fun onTrackClick(track: Track) {
        if (debouncer.clickDebounce()) {
            viewModel.addToSearchHistory(track)
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK, track)
            startActivity(intent)
        }
    }


    private fun showMessage(massage: String) {
        if (massage == ScreenState.NOT_FOUND) {
            binding.placeholderImage.setImageResource(R.drawable.not_found)
            binding.placeholderText.text = getString(R.string.placeholder_not_found)
            binding.updateButton.isVisible = false
        } else {
            binding.placeholderImage.setImageResource(R.drawable.search_error)
            binding.placeholderText.text = getString(R.string.placeholder_error)
            binding.updateButton.isVisible = true
        }


    }

    companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
        const val TRACK = "TRACK"

    }

    private fun setScreenState(state: ScreenState) {
        binding.searchHistory.isVisible = state is ScreenState.SearchHistory
        binding.searchList.isVisible = state is ScreenState.SearchResults
        binding.placeholder.isVisible = state is ScreenState.Placeholder
        binding.progressBar.isVisible = state is ScreenState.Loading
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


    private fun searchHistoryLayoutVisibility(s: CharSequence?) {
        if (binding.search.hasFocus() && s.isNullOrEmpty()) {
            viewModel.showHistory()

        } else {
            viewModel.setEmptyState()
        }
    }
}