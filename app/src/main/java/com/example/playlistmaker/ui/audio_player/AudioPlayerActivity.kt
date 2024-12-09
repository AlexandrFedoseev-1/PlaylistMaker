package com.example.playlistmaker.ui.audio_player



import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.Runnable

class AudioPlayerActivity : AppCompatActivity() {

    private var playerState = STATE_DEFAULT


    private lateinit var player: AudioPlayerInteractor
    private lateinit var previewUrl: String
    private lateinit var handler: Handler
    private lateinit var playbackRunnable: Runnable
    private lateinit var binding: ActivityAudioPlayerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val trackData: Track? = intent.getParcelableExtra("TRACK")

        handler = Handler(Looper.getMainLooper())
        previewUrl = trackData?.previewUrl.toString()

        player = Creator.provideAudioPlayerInteractor()

        player.prepare(previewUrl,
            onPrepared = {
                playerState = STATE_PREPARED
            },
            onCompletion = {
                binding.playButton.setBackgroundResource(R.drawable.play_button)
                handler.removeCallbacks(playbackRunnable)
                binding.demoPlayTime.text = "00:00"
                playerState = STATE_PREPARED
            }
        )

        playbackRunnable = Runnable { updatePlaybackTime() }

        Glide.with(applicationContext)
            .load(trackData?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")).transform(
                RoundedCorners(
                    applicationContext.resources
                        .getDimensionPixelSize(R.dimen.album_image_corner_radius)
                )
            )
            .placeholder(R.drawable.album_placeholder)
            .into(binding.albumImage)

        binding.playButton.setOnClickListener {
            playbackControl()
        }

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

        binding.backButton.setOnClickListener {
            super.finish()
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(playbackRunnable)
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()

        player.release()
        handler.removeCallbacks(playbackRunnable)
    }



    private fun startPlayer() {
        player.start()
        handler.post(playbackRunnable)
        binding.playButton.setBackgroundResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        handler.removeCallbacks(playbackRunnable)
        player.pause()
        binding.playButton.setBackgroundResource(R.drawable.play_button)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun updatePlaybackTime() {
        binding.demoPlayTime.text = player.getCurrentPosition()
        handler.postDelayed(playbackRunnable, DELAY)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 300L

    }
}