package com.mainp.musicapp.data.repository

import com.mainp.musicapp.api.ApiService
import com.mainp.musicapp.data.entity.Song

//private val apiService = ApiService.create()
interface SongRepository {
    suspend fun fetchSongs(): List<Song>
//    private val apiService = ApiService.create()

//    suspend fun fetchSongs(): List<Song> {
//        val response = apiService.getSongs()
//        if (response.isSuccessful) {
//            return response.body() ?: emptyList()
//        } else {
//            throw Exception("Failed to fetch songs")
//        }
//    }
}