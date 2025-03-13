package com.mainp.musicapp.presentation.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.mainp.musicapp.R
import com.mainp.musicapp.databinding.ActivityDemoBinding
import androidx.activity.viewModels
import com.mainp.musicapp.api.ApiService
import com.mainp.musicapp.data.repository.SongRepositoryApiImpl
import com.mainp.musicapp.presentation.viewmodel.SongViewModel
import com.mainp.musicapp.presentation.viewmodel.SongViewModelFactory

@androidx.media3.common.util.UnstableApi

class DemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDemoBinding
    private var isPlaying = false
    private var isRepeat = false
    private lateinit var player: ExoPlayer
    private val viewModel by viewModels<SongViewModel> {
        SongViewModelFactory(
            SongRepositoryApiImpl(
                ApiService.create()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val songUrl = intent.getStringExtra("songUrl") ?: ""
        val title = intent.getStringExtra("title") ?: "Unknown Title"
        val artist = intent.getStringExtra("artist") ?: "Unknown Artist"
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""
//        val songUrl = "https://a128-z3.zmdcdn.me/31cb17656c5146f10de0247036f2772d?authen=exp=1742007464~acl=/31cb17656c5146f10de0247036f2772d*~hmac=7328a73d6730b91c036e12b8e769f17c&fs=MHx3ZWJWNXwxMDMdUngNTmUsICdUngMjIxLjI3"
        viewModel.fetchDirectSongUrl("https://zingmp3.vn/${songUrl}")
        viewModel.directSongUrl.observe(this) { songUrl ->
            if (songUrl != null) {
                playMusic(songUrl)
            }
        }

        binding.songTitle.text = title
        binding.songArtist.text = artist
        Glide.with(this).load(imageUrl).into(binding.songImage)

        setupPlayer(songUrl)

        binding.btnPlay.setOnClickListener {
            if (isPlaying) {
                player.pause()
                binding.btnPlay.setImageResource(R.drawable.ic_pause)
            } else {
                player.play()
                binding.btnPlay.setImageResource(R.drawable.ic_heart_grey)
            }
            isPlaying = !isPlaying
        }

        binding.btnRepeat.setOnClickListener {
            isRepeat = !isRepeat
            player.repeatMode = if (isRepeat) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
            binding.btnRepeat.setImageResource(if (isRepeat) R.drawable.ic_replay else R.drawable.ic_replay_black)
        }
    }
    fun playMusic(songUrl: String) {
        setupPlayer(songUrl)
    }

    private fun setupPlayer(songUrl: String) {
        player = ExoPlayer.Builder(this).build()
        val mediaItem = MediaItem.Builder()
            .setUri(songUrl)
            .build()
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        isPlaying = true
        binding.btnPlay.setImageResource(R.drawable.ic_pause)
    }
}
