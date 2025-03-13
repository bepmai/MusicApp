package com.mainp.musicapp.presentation.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.mainp.musicapp.R
import com.mainp.musicapp.databinding.ActivityDemoBinding
import android.media.MediaPlayer
@androidx.media3.common.util.UnstableApi

class DemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDemoBinding
    private var isPlaying = false
    private var isRepeat = false
    private lateinit var player: ExoPlayer

    private var mediaPlayer: MediaPlayer = MediaPlayer()
//    private var boolean isPlay = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        https://m.vuiz.net/getlink/mp3zing/
//        val BASE_URL = "https://m.vuiz.net/mp3zing/bai-hat/Mat-Ket-Noi-Duong-Domic/Z7E9OI09.html"
        //        if (!songUrl.startsWith("http")) {
//            songUrl = BASE_URL + songUrl
//        }
        val songUrl = intent.getStringExtra("songUrl") ?: ""
        val fullSongUrl = "https://zingmp3.vn/bai-hat/$songUrl"

        Log.d("DemoActivity", "Full Song URL: $fullSongUrl")


        val title = intent.getStringExtra("title")!!
        val artist = intent.getStringExtra("artist")!!
        val imageUrl = intent.getStringExtra("imageUrl")!!
//        Log.d("DemoActivity", "Song URL: $songUrl")


        binding.songTitle.text = title
        binding.songArtist.text = artist
        Glide.with(this).load(imageUrl).into(binding.songImage)

        player = ExoPlayer.Builder(this).build()
        val mediaItem = MediaItem.Builder()
            .setUri(songUrl)
            .build()
        player.setMediaItem(mediaItem)

        player.prepare()
        player.play()


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

//        player.addListener(object : Player.Listener {
//            override fun onPlayerError(error: PlaybackException) {
//                Log.e("DemoActivity", "Lỗi phát nhạc: ${error.errorCodeName} - ${error.message}")
//            }
//        })
    }
}