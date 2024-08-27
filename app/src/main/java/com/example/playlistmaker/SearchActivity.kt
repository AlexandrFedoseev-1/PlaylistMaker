package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private var savedText: String? = null
    private val trackBaseUrl = BASE_URL
    private val retrofit = Retrofit.Builder().baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val trackService = retrofit.create(TrackSearchApi::class.java)
    private val trackList = ArrayList<Track>()

    private lateinit var placeholderMessage: LinearLayout
    private lateinit var backButton: ImageView
    private lateinit var placeholderImage: ImageView
    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var updateButton: Button
    private lateinit var placeholderText: TextView


    private val searchAdapter = SearchAdapter(trackList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        backButton = findViewById(R.id.back_button)
        searchEditText = findViewById(R.id.search)
        clearButton = findViewById(R.id.clear_text)
        placeholderImage = findViewById(R.id.placeholder_image)
        placeholderMessage = findViewById(R.id.placeholder)
        searchRecyclerView = findViewById(R.id.search_list)
        updateButton = findViewById(R.id.update_button)
        placeholderText = findViewById(R.id.placeholder_text)
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        searchRecyclerView.adapter = searchAdapter

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trackSearch()
                true
            }
            false
        }


        backButton.setOnClickListener {
            super.finish()
        }

        updateButton.setOnClickListener{
            trackSearch()
        }

        clearButton.setOnClickListener {
            searchEditText.setText("")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            trackList.clear()
            searchAdapter.notifyDataSetChanged()
            placeholderMessage.visibility =View.GONE
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
    private fun trackSearch(){
        if (searchEditText.text.isNotEmpty()) {
            trackService.search(searchEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            trackList.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.addAll(response.body()?.results!!)
                                searchAdapter.notifyDataSetChanged()
                            }
                            if (trackList.isEmpty()) {
                                showMessage(
                                    getString(R.string.placeholder_not_found),
                                    response.code()
                                )
                            } else {
                                showMessage("", response.code())
                            }
                        } else {
                            showMessage(
                                getString(R.string.placeholder_error),
                                response.code()
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
            placeholderMessage.visibility = View.VISIBLE
            trackList.clear()
            searchAdapter.notifyDataSetChanged()
            placeholderText.text = text
            Toast.makeText(applicationContext, code.toString(), Toast.LENGTH_LONG)
                .show()
            if (code == 200) {
                placeholderImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.not_found
                    )
                )
                updateButton.visibility = View.GONE
            } else {
                placeholderImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.search_error
                    )
                )
                updateButton.visibility = View.VISIBLE
            }
        } else {
            placeholderMessage.visibility = View.GONE
        }
    }

    companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
        const val BASE_URL = "https://itunes.apple.com"
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
}