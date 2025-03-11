package com.mainp.musicapp.data.repository

import com.mainp.musicapp.api.ApiService
import com.mainp.musicapp.data.entity.Song

class SongRepositoryApiImpl (private val api: ApiService):SongRepository {
    override suspend fun fetchSongs(): List<Song> {
        val response = api.getSongs()
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }
}