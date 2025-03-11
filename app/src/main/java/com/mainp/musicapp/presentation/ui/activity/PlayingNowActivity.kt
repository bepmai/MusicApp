package com.mainp.musicapp.presentation.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.mainp.musicapp.data.entity.Song
import com.mainp.musicapp.databinding.ActivityPlayingNowBinding
import com.mainp.musicapp.presentation.ui.adapter.PlayingNowAdapter
import com.mainp.musicapp.presentation.ui.adapter.SongAdapter

class PlayingNowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayingNowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayingNowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val albumList = listOf(
            Song("", "aa", "","https://www.gstatic.com/devrel-devsite/prod/v630f393d749cf7dff36a03367007d65915d987bf69b5409e6d70ed7311ba2c07/android/images/lockup.svg"),
            Song("", "ab", "","https://www.gstatic.com/devrel-devsite/prod/v630f393d749cf7dff36a03367007d65915d987bf69b5409e6d70ed7311ba2c07/android/images/lockup.svg"),
            Song("", "ac", "","https://www.gstatic.com/devrel-devsite/prod/v630f393d749cf7dff36a03367007d65915d987bf69b5409e6d70ed7311ba2c07/android/images/lockup.svg"),
            Song("", "ad", "","https://www.gstatic.com/devrel-devsite/prod/v630f393d749cf7dff36a03367007d65915d987bf69b5409e6d70ed7311ba2c07/android/images/lockup.svg")
        )

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = SongAdapter(albumList)

        viewPager.setPageTransformer { page, position ->
            val absPosition = Math.abs(position)
            page.scaleY = 1 - (0.2f * absPosition)
            page.alpha = 1 - (0.3f * absPosition)
        }

    }
}