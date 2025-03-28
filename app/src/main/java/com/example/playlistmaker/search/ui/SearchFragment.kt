package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.debounce
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {
    private var savedText: String = ""
    private val viewModel by viewModel<SearchViewModel>()

    private val searchAdapter by lazy {
        SearchAdapter { track ->
            onTrackClickDebounce(track)
        }
    }
    private val historyAdapter by lazy {
        SearchAdapter { track ->
            onTrackClickDebounce(track)
        }
    }


    private lateinit var binding: FragmentSearchBinding


    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchList.adapter = searchAdapter

        binding.historySearchList.adapter = historyAdapter

        onTrackClickDebounce =
            debounce(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { track ->
                viewModel.addToSearchHistory(track)
                val action =
                    SearchFragmentDirections.actionSearchFragmentToAudioPlayerFragment(track)
                findNavController().navigate(action)

            }


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



        binding.updateButton.setOnClickListener {
            viewModel.trackSearch(binding.search.text.toString())
        }

        binding.clearText.setOnClickListener {
            binding.search.setText("")
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(binding.search.windowToken, 0)
            searchHistoryLayoutVisibility("")
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearText.isVisible = !s.isNullOrEmpty()
                searchHistoryLayoutVisibility(s ?: "")
                viewModel.trackSearchDebounce(s?.toString() ?: "")


            }

            override fun afterTextChanged(s: Editable?) {
                savedText = s.toString()
            }

        }

        binding.search.addTextChangedListener(searchTextWatcher)


        viewModel.screenState.observe(viewLifecycleOwner) { state ->
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

    override fun onStart() {
        super.onStart()
        viewModel.loadSearchHistory()
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.search.setText(savedText)
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


    private fun setScreenState(state: ScreenState) {
        binding.searchHistory.isVisible = state is ScreenState.SearchHistory
        binding.searchList.isVisible = state is ScreenState.SearchResults
        binding.placeholder.isVisible = state is ScreenState.Placeholder
        binding.progressBar.isVisible = state is ScreenState.Loading
    }

    private fun searchHistoryLayoutVisibility(s: CharSequence?) {
        if (!binding.search.hasFocus()) {
            Log.d("SearchFragment", "Field is not focused – skip state change")
            return
        }

        if (s.isNullOrEmpty()) {
            viewModel.showHistory()
        } else {
            viewModel.setEmptyState()
            Log.d("SearchFragment", "searchHistoryLayoutVisibility: setEmptyState called")
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
        fun newInstance() =
            SearchFragment()
    }
}