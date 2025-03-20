package com.mainp.musicapp.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mainp.musicapp.api.ApiService
import com.mainp.musicapp.data.entity.FavoriteSong
import com.mainp.musicapp.data.entity.Song
import com.mainp.musicapp.data.repository.FavoriteSongRepositoryImpl
import com.mainp.musicapp.data.repository.SongRepositoryApiImpl
import com.mainp.musicapp.data.room.AppDatabase
import com.mainp.musicapp.databinding.FragmentFavoriteSongBinding
import com.mainp.musicapp.presentation.ui.activity.PlayingNowActivity
import com.mainp.musicapp.presentation.ui.adapter.FavoriteSongAdapter
import com.mainp.musicapp.presentation.viewmodel.SongViewModel
import com.mainp.musicapp.presentation.viewmodel.SongViewModelFactory

@androidx.media3.common.util.UnstableApi


class FavoriteSongFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteSongBinding
    private lateinit var favoriteSongAdapter: FavoriteSongAdapter

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
        binding = FragmentFavoriteSongBinding.inflate(inflater, container, false)

        binding.rvSongs.layoutManager = GridLayoutManager(requireContext(), 2)
        favoriteSongAdapter = FavoriteSongAdapter()
        binding.rvSongs.adapter = favoriteSongAdapter

        viewModel.favoriteSongs.observe(viewLifecycleOwner) { favoriteSongs ->
            favoriteSongAdapter.submitList(favoriteSongs)
        }

        favoriteSongAdapter.setOnItemClickListener { favoriteSong ->
            val song = Song(
                id = favoriteSong.id,
                title = favoriteSong.title,
                artist = favoriteSong.artist,
                thumbnail = favoriteSong.thumbnail,
                path = favoriteSong.path,
                duration = favoriteSong.duration
            )

            Log.d("FavoriteSongFragment", "Bài hát được chọn: ${song.title}, URL: ${song.path}")
            viewModel.setCurrentSong(song)

            val songList = favoriteSongAdapter.getCurrentFavoriteSongs().map {
                Song(it.id, it.title, it.artist, it.thumbnail, it.path, it.duration)
            }

            val selectedIndex = songList.indexOf(song)

            val intent = Intent(requireContext(), PlayingNowActivity::class.java).apply {
                putExtra("SONG_LIST", ArrayList(songList))
                putExtra("CURRENT_INDEX", selectedIndex)
                putExtra("FAVORITE_SONG", favoriteSong)
            }
            startActivity(intent)
        }


        return binding.root
    }

}