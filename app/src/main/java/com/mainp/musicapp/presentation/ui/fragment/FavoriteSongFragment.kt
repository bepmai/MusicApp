package com.mainp.musicapp.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import androidx.fragment.app.activityViewModels
import com.mainp.musicapp.api.ApiService
import com.mainp.musicapp.data.entity.FavoriteSong
import com.mainp.musicapp.data.repository.FavoriteSongRepositoryImpl
import com.mainp.musicapp.data.repository.SongRepositoryApiImpl
import com.mainp.musicapp.data.room.AppDatabase
import com.mainp.musicapp.databinding.FragmentFavoriteSongBinding
import com.mainp.musicapp.presentation.ui.adapter.FavoriteSongAdapter
import com.mainp.musicapp.presentation.ui.adapter.SongAdapter
import com.mainp.musicapp.presentation.viewmodel.SongViewModel
import com.mainp.musicapp.presentation.viewmodel.SongViewModelFactory
@androidx.media3.common.util.UnstableApi


class FavoriteSongFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteSongBinding
    private lateinit var favoriteSongAdapter: FavoriteSongAdapter

    private val viewModel by viewModels<SongViewModel> {
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
        binding = FragmentFavoriteSongBinding.inflate(inflater, container, false)
        favoriteSongAdapter = FavoriteSongAdapter()

        binding.rvSongs.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvSongs.adapter = favoriteSongAdapter

        viewModel.favoriteSongs.observe(viewLifecycleOwner) { favoriteSongs ->
            favoriteSongAdapter.submitList(favoriteSongs)
        }

        return binding.root
    }
}