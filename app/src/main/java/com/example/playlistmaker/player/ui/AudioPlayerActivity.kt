package com.example.playlistmaker.player.ui


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerActivity : AppCompatActivity() {
    private val viewModel by viewModel<AudioPlayerViewModel>()

    private lateinit var binding: ActivityAudioPlayerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val trackData:Track? = intent.getParcelableExtra("TRACK")
        viewModel.setValues(trackData?.isFavorite,trackData?.previewUrl.toString())

        setupTrackInfo(trackData)

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }
        binding.addFavoritesButton.setOnClickListener {
            viewModel.onFavoriteClicked(trackData)
        }
        binding.toolbar.setNavigationOnClickListener { super.finish() }

        viewModel.playerState.observe(this) { state ->
            binding.playButton.isEnabled = state.isPlayButtonEnabled
            binding.playButton.setBackgroundResource(state.buttonImage)
            binding.demoPlayTime.text = state.progress
        }
        viewModel.isFavoriteLiveData.observe(this){ isFavorite ->
            if (isFavorite){
                binding.addFavoritesButton.setBackgroundResource(R.drawable.add_favorite_button_on)
            }else{
                binding.addFavoritesButton.setBackgroundResource(R.drawable.add_favorites_button)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
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