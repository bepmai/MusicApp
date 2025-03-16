package com.mainp.musicapp.presentation.ui.activity

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.mainp.musicapp.R
import com.mainp.musicapp.api.ApiService
import com.mainp.musicapp.data.entity.Song
import com.mainp.musicapp.data.repository.SongRepositoryApiImpl
import com.mainp.musicapp.databinding.ActivityPlayingNowBinding
import com.mainp.musicapp.presentation.ui.adapter.PlayingNowAdapter
import com.mainp.musicapp.presentation.viewmodel.SongViewModel
import com.mainp.musicapp.presentation.viewmodel.SongViewModelFactory
import android.app.DownloadManager
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat



class PlayingNowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayingNowBinding
    lateinit var adapter: PlayingNowAdapter

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
        binding = ActivityPlayingNowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentIndex = intent.getIntExtra("position", 0)
        val songUrl = intent.getStringExtra("songUrl") ?: ""
        val title = intent.getStringExtra("title") ?: "Unknown Title"
        val artist = intent.getStringExtra("artist") ?: "Unknown Artist"
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""

        binding.tvTitle.text = title
        binding.tvArtist.text = artist

        adapter = PlayingNowAdapter(listOf(imageUrl))
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.setPageTransformer(ZoomOutPageTransformer())
        binding.viewPager.clipChildren = false
        binding.viewPager.clipToPadding = false
        binding.viewPager.setPadding(100, 0, 100, 0)


        binding.viewPager.isUserInputEnabled = false
        val recyclerView = binding.viewPager.getChildAt(0) as RecyclerView
        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return true
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

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

        binding.ivNext.setOnClickListener { nextSong() }
        binding.ivPrev.setOnClickListener { previousSong() }

        binding.ivPlay.setOnClickListener {
            if (isPlaying) {
                player.pause()
                binding.ivPlay.setImageResource(R.drawable.ic_play_arrow_filled)
            } else {
                player.play()
                binding.ivPlay.setImageResource(R.drawable.ic_pause)
            }
            isPlaying = !isPlaying
        }

        binding.ivReplay.setOnClickListener {
            isRepeat = !isRepeat
            player.repeatMode = if (isRepeat) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
            binding.ivReplay.setImageResource(if (isRepeat) R.drawable.ic_replay else R.drawable.ic_replay_black)

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

        binding.ivDownl.setOnClickListener {
            val song = songList[currentIndex]
            downloadAndSaveSong(song)
        }

    }

    class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            val scaleFactor = 0.85f + (1 - kotlin.math.abs(position)) * 0.15f
            val alphaFactor = 0.5f + (1 - kotlin.math.abs(position)) * 0.5f

            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
            view.alpha = alphaFactor

            val pageWidth = view.width
            val spacing = pageWidth * 0.02f

            if (position < 0) {
                view.translationX = -spacing
            } else if (position > 0) {
                view.translationX = spacing
            } else {
                view.translationX = 0f
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
        binding.ivPlay.setImageResource(R.drawable.ic_pause)

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

    private fun setupCurrentSong() {
        if (songList.isNotEmpty() && currentIndex in songList.indices) {
            val previousIndex = if (currentIndex > 0) currentIndex - 1 else currentIndex
            val nextIndex = if (currentIndex < songList.size - 1) currentIndex + 1 else currentIndex

            val songImages = listOf(
                songList[previousIndex].thumbnail,
                songList[currentIndex].thumbnail,
                songList[nextIndex].thumbnail
            )

            adapter.updateImageList(songImages)
            binding.viewPager.setCurrentItem(1, false)

            binding.tvTitle.text = songList[currentIndex].title
            binding.tvArtist.text = songList[currentIndex].artist

            viewModel.fetchDirectSongUrl("https://zingmp3.vn/${songList[currentIndex].path}")
            viewModel.directSongUrl.observe(this) { directUrl ->
                if (!directUrl.isNullOrBlank()) {
                    playMusic(directUrl)
                } else {
                    Log.e("PLAYER_ERROR", "Không lấy được URL phát nhạc!")
                }
            }
        }
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

    private fun downloadAndSaveSong(song: Song) {
        viewModel.fetchDirectSongUrl("https://zingmp3.vn/${song.path}")

        viewModel.directSongUrl.observe(this) { directUrl ->
            if (!directUrl.isNullOrBlank()) {
                downloadSong(directUrl, song.title)
            } else {
                Log.e("DOWNLOAD_ERROR", "Không lấy được URL bài hát để tải!")
            }
        }
    }

    private fun downloadSong(url: String, title: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(title)
            .setDescription("Đang tải nhạc...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, "$title.mp3")

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        Toast.makeText(this, "Bắt đầu tải bài hát: $title", Toast.LENGTH_SHORT).show()
    }

    private fun saveSongToOfflineList(song: Song) {
        val sharedPreferences = getSharedPreferences("OfflineMusic", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val json = sharedPreferences.getString("songList", "[]")
        val type = object : TypeToken<MutableList<Song>>() {}.type
        val songList: MutableList<Song> = gson.fromJson(json, type)

        songList.add(song)

        val updatedJson = gson.toJson(songList)
        editor.putString("songList", updatedJson)
        editor.apply()
    }
    private fun checkPermissionAndDownload(url: String, title: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                downloadSong(url, title)
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1001
                )
            }
        } else {
            downloadSong(url, title)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Quyền được cấp! Hãy thử tải lại nhạc.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Cần cấp quyền để tải nhạc!", Toast.LENGTH_SHORT).show()
        }
    }


}