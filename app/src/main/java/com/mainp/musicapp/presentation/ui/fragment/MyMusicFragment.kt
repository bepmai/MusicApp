package com.mainp.musicapp.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mainp.musicapp.R
import com.mainp.musicapp.data.entity.Song

class MyMusicFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_music, container, false)
    }

    private fun loadOfflineSongs() {
        val sharedPreferences = requireContext().getSharedPreferences("OfflineMusic", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("songList", "[]")
        val type = object : TypeToken<List<Song>>() {}.type
        val songList: List<Song> = gson.fromJson(json, type)

        // Hiển thị danh sách trong RecyclerView
//        adapter.updateSongList(songList)
    }
}