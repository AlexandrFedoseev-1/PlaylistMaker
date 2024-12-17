package com.example.playlistmaker.player.ui


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.search.domain.models.Track

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: AudioPlayerViewModel
    private lateinit var binding: ActivityAudioPlayerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val trackData: Track? = intent.getParcelableExtra("TRACK")
        val previewUrl = trackData?.previewUrl.toString()
        viewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory(previewUrl)
        )[AudioPlayerViewModel::class.java]



        setupTrackInfo(trackData)

        binding.playButton.setOnClickListener {
            playbackControl()
        }
        binding.toolbar.setNavigationOnClickListener { super.finish() }

        viewModel.playerState.observe(this) { state ->
            when (state) {
                is AudioPlayerScreenState.Default -> {
                    binding.playButton.setBackgroundResource(R.drawable.play_button)
                    binding.demoPlayTime.text = "00:00"
                }

                is AudioPlayerScreenState.Prepared -> {
                    binding.playButton.setBackgroundResource(R.drawable.play_button)
                }

                is AudioPlayerScreenState.Playing -> {
                    binding.playButton.setBackgroundResource(R.drawable.pause_button)
                    binding.demoPlayTime.text = state.currentPosition
                }

                is AudioPlayerScreenState.Paused -> {
                    binding.playButton.setBackgroundResource(R.drawable.play_button)
                    binding.demoPlayTime.text = state.currentPosition
                }
            }

        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun playbackControl() {
        when (viewModel.playerState.value) {
            is AudioPlayerScreenState.Playing -> viewModel.pausePlayer()
            is AudioPlayerScreenState.Prepared, is AudioPlayerScreenState.Paused -> viewModel.startPlayer()
            else -> Unit
        }
    }


    private fun setupTrackInfo(trackData: Track?) {
        Glide.with(applicationContext)
            .load(trackData?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")).transform(
                RoundedCorners(
                    applicationContext.resources
                        .getDimensionPixelSize(R.dimen.album_image_corner_radius)
                )
            )
            .placeholder(R.drawable.album_placeholder)
            .into(binding.albumImage)

        binding.trackName.text = trackData?.trackName
        binding.artistName.text = trackData?.artistName
        binding.trackTime.text = trackData?.trackTime
        binding.releaseDate.text = trackData?.releaseDate?.substring(0, 4)
        binding.genreName.text = trackData?.primaryGenreName
        binding.country.text = trackData?.country
        if (trackData?.collectionName.isNullOrEmpty()) {
            binding.collectionNameGroup.visibility = View.GONE
        } else {
            binding.collectionName.text = trackData?.collectionName
            binding.collectionNameGroup.visibility = View.VISIBLE
        }
    }


}