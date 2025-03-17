package com.mainp.musicapp.data.repository

import androidx.lifecycle.LiveData
import com.mainp.musicapp.data.entity.FavoriteSong

interface FavoriteSongRepository {
    fun getAllFavoriteSong(): LiveData<List<FavoriteSong>>
    suspend fun getFavoriteSongById(songId: String): FavoriteSong?
    suspend fun insert(song: FavoriteSong)
    suspend fun delete(song: FavoriteSong)
}