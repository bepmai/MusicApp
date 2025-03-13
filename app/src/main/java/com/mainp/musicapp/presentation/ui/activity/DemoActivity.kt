package com.mainp.musicapp.presentation.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
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
import com.mainp.musicapp.data.entity.Song
import com.mainp.musicapp.data.repository.SongRepositoryApiImpl
import com.mainp.musicapp.presentation.viewmodel.SongViewModel
import com.mainp.musicapp.presentation.viewmodel.SongViewModelFactory
import android.os.Handler
import android.os.Looper

@androidx.media3.common.util.UnstableApi

class DemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDemoBinding
    private var isPlaying = false
    private var isRepeat = false
    private lateinit var player: ExoPlayer
    private var songList: List<Song> = emptyList()
    private var currentIndex = 0
    private val handler = Handler(Looper.getMainLooper())
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

        currentIndex = intent.getIntExtra("position", 0)
        val songUrl = intent.getStringExtra("songUrl") ?: ""
        val title = intent.getStringExtra("title") ?: "Unknown Title"
        val artist = intent.getStringExtra("artist") ?: "Unknown Artist"
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""

        binding.tvTitle.text = title
        binding.tvArtist.text = artist
        Glide.with(this).load(imageUrl).into(binding.ivImage)

        viewModel.fetchDirectSongUrl("https://zingmp3.vn/${songUrl}")
        viewModel.directSongUrl.observe(this) { directUrl ->
            if (!directUrl.isNullOrBlank()) {
                playMusic(directUrl)
                Log.e("PLAYER_ERROR", "lấy được URL bài đầu tiên!${directUrl}")
            } else {
                Log.e("PLAYER_ERROR", "Không lấy được URL bài đầu tiên!")
            }
        }

        viewModel.getAllSongApi()
        viewModel.songs.observe(this) { songs ->
            songList = songs
            setupCurrentSong()
        }

        binding.btnNext.setOnClickListener { nextSong() }
        binding.btnPrev.setOnClickListener { previousSong() }

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

            Log.d("MUSIC_PLAYER", "Repeat mode: ${if (isRepeat) "ON" else "OFF"}")
        }

        binding.sbSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser && ::player.isInitialized) {
                    player.seekTo(progress.toLong())
                    binding.tvCurrentTime.text = formatTime(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupCurrentSong() {
        if (songList.isNotEmpty() && currentIndex in songList.indices) {
            val song = songList[currentIndex]
            binding.tvTitle.text = song.title
            binding.tvArtist.text = song.artist
            Glide.with(this).load(song.thumbnail).into(binding.ivImage)

            viewModel.fetchDirectSongUrl("https://zingmp3.vn/${song.path}")
            viewModel.directSongUrl.observe(this) { directUrl ->
                if (!directUrl.isNullOrBlank()) {
                    playMusic(directUrl)
                } else {
                    Log.e("PLAYER_ERROR", "Không lấy được URL phát nhạc!")
                }
            }
        }
    }

    private fun playMusic(songUrl: String) {
        if (!::player.isInitialized) {
            player = ExoPlayer.Builder(this).build()
        }

        player.stop()
        player.clearMediaItems()

        val mediaItem = MediaItem.Builder()
            .setUri(songUrl)
            .build()

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        isPlaying = true
        binding.btnPlay.setImageResource(R.drawable.ic_pause)

        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    val duration = player.duration
                    binding.sbSeekbar.max = duration.toInt()
                    binding.tvTotalTime.text = formatTime(duration)
                }
            }
        })

        handler.postDelayed(updateSeekBar, 1000)
    }


    private fun nextSong() {
        if (songList.isNotEmpty() && currentIndex < songList.size - 1) {
            currentIndex++
            setupCurrentSong()
        }
    }

    private fun previousSong() {
        if (songList.isNotEmpty() && currentIndex > 0) {
            currentIndex--
            setupCurrentSong()
        }
    }

    private val updateSeekBar = object : Runnable {
        override fun run() {
            if (::player.isInitialized && player.isPlaying) {
                val currentPos = player.currentPosition
                binding.sbSeekbar.progress = currentPos.toInt()
                binding.tvCurrentTime.text = formatTime(currentPos)
            }
            handler.postDelayed(this, 1000)
        }
    }

    private fun formatTime(ms: Long): String {
        val minutes = (ms / 1000) / 60
        val seconds = (ms / 1000) % 60
        return String.format("%d:%02d", minutes, seconds)
    }

}
