package com.mainp.musicapp.data.repository

import android.util.Log
import com.mainp.musicapp.api.ApiService
import com.mainp.musicapp.data.entity.Song

class SongRepositoryApiImpl(private val api: ApiService) : SongRepository {
    override suspend fun fetchSongs(): List<Song> {
        val response = api.getSongs()
        if (response.isSuccessful) {
            val songs = response.body() ?: emptyList()
            songs.forEach { song ->
                val fullPath = "https://zingmp3.vn${song.path}"
                Log.d("API_RESPONSE", "Đường dẫn đầy đủ: $fullPath")
            }
//            Log.d("API_RESPONSE", "Danh sách bài hát: $songs")
            return songs
        } else {
            Log.e("API_ERROR", "Lỗi API: ${response.errorBody()?.string()}")
            return emptyList()
        }
    }
}