package com.mainp.musicapp.presentation.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mainp.musicapp.api.ApiService
import com.mainp.musicapp.data.repository.SongRepositoryApiImpl
import com.mainp.musicapp.databinding.FragmentExploreBinding
import com.mainp.musicapp.presentation.viewmodel.SongViewModel
import com.mainp.musicapp.presentation.viewmodel.SongViewModelFactory
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mainp.musicapp.data.repository.FavoriteSongRepositoryImpl
import com.mainp.musicapp.data.room.AppDatabase
import com.mainp.musicapp.presentation.ui.activity.PlayingNowActivity
import com.mainp.musicapp.presentation.ui.adapter.SongAdapter
@androidx.media3.common.util.UnstableApi

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private lateinit var songAdapter: SongAdapter

    private val viewModel by activityViewModels<SongViewModel> {
        SongViewModelFactory(
            SongRepositoryApiImpl(ApiService.create()),
            FavoriteSongRepositoryImpl(
                AppDatabase.getDatabase(requireContext()).favoriteSongDao()
            )
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)

        binding.rvSongs.layoutManager = GridLayoutManager(requireContext(), 2)
        songAdapter = SongAdapter()
        binding.rvSongs.adapter = songAdapter


        viewModel.getAllSongApi()

        viewModel.songs.observe(viewLifecycleOwner) { songs ->
            songAdapter.submitList(songs)
        }

        songAdapter.setOnItemClickListener { song ->
            Log.d("ExploreFragment", "Bài hát được chọn: ${song.title}, URL: ${song.path}")
            viewModel.setCurrentSong(song)

            val songList = ArrayList(songAdapter.getCurrentSongs())
            val selectedIndex = songList.indexOf(song)

            val intent = Intent(requireContext(), PlayingNowActivity::class.java).apply {
                putExtra("SONG_LIST", songList)
                putExtra("CURRENT_INDEX", selectedIndex)
            }

            Log.d("ExploreFragment", "Đang mở PlayingNowActivity với danh sách ${songList.size} bài hát")

            startActivity(intent)
        }



        return binding.root
    }
}
