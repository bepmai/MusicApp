package com.mainp.musicapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mainp.musicapp.api.ApiService
import com.mainp.musicapp.data.repository.SongRepositoryApiImpl
import com.mainp.musicapp.databinding.FragmentExploreBinding
import com.mainp.musicapp.presentation.viewmodel.SongViewModel
import com.mainp.musicapp.presentation.viewmodel.SongViewModelFactory
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mainp.musicapp.presentation.ui.adapter.SongAdapter

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private lateinit var songAdapter: SongAdapter

    private val viewModel by viewModels<SongViewModel> {
        SongViewModelFactory(
            SongRepositoryApiImpl(
                ApiService.create()
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

        return binding.root
    }
}
