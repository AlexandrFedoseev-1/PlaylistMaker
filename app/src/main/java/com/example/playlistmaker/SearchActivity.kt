package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    private var savedText: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val backButton = findViewById<ImageView>(R.id.back_button)
        val searchEditText = findViewById<EditText>(R.id.search)
        val clearButton = findViewById<ImageView>(R.id.clear_text)
        val searchRecyclerView = findViewById<RecyclerView>(R.id.search_list)
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        val searchAdapter = SearchAdapter(trackList)
        searchRecyclerView.adapter = searchAdapter
        backButton.setOnClickListener {
            super.finish()
        }

        clearButton.setOnClickListener {
            searchEditText.setText("")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)

            }

            override fun afterTextChanged(s: Editable?) {
                savedText = s?.toString()
            }

        }

        searchEditText.addTextChangedListener(searchTextWatcher)


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT, savedText)
    }

    companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
        val trackList = listOf(
            Track(
                trackName = "Smells Like Teen Spirit",
                artistName = "Nirvana",
                trackTime = "5:01",
                artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                trackName = "Billie Jean",
                artistName = "Michael Jackson",
                trackTime = "4:35",
                artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            ),
            Track(
                trackName = "Stayin' Alive",
                artistName = "Bee Gees",
                trackTime = "4:10",
                artworkUrl100 = "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                trackName = "Whole Lotta Love",
                artistName = "Led Zeppelin",
                trackTime = "5:33",
                artworkUrl100 = "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
            ),
            Track(
                trackName = "Sweet Child O'Mine",
                artistName = "Guns N' Roses",
                trackTime = "5:03",
                artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc48"
            )
        )
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
}