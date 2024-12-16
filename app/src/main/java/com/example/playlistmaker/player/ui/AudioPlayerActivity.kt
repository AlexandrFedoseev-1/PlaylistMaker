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

        viewModel.playerState.observe(this){ state ->
            updatePlayButton(state)
        }
        viewModel.currentPosition.observe(this){ time ->
            binding.demoPlayTime.text = time
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun updatePlayButton(state: Int) {
        binding.playButton.setBackgroundResource(
            if (state == AudioPlayerViewModel.STATE_PLAYING) R.drawable.pause_button
            else R.drawable.play_button
        )
    }

    private fun playbackControl() {
        when (viewModel.playerState.value) {
            AudioPlayerViewModel.STATE_PLAYING -> {
                viewModel.pausePlayer()
            }

            AudioPlayerViewModel.STATE_PREPARED, AudioPlayerViewModel.STATE_PAUSED -> {
                viewModel.startPlayer()
            }
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