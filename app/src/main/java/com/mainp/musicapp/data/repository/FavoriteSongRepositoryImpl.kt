package com.mainp.musicapp.data.repository

import androidx.lifecycle.LiveData
import com.mainp.musicapp.data.dao.FavoriteSongDao
import com.mainp.musicapp.data.entity.FavoriteSong

class FavoriteSongRepositoryImpl(
    private val favoriteSongDao: FavoriteSongDao
) : FavoriteSongRepository {


    override fun getAllFavoriteSong(): LiveData<List<FavoriteSong>> =
        favoriteSongDao.getAllFavoriteSong()

    override suspend fun getFavoriteSongById(songId: String): FavoriteSong? =
        favoriteSongDao.getById(songId)

    override suspend fun insert(song: FavoriteSong) {
        favoriteSongDao.insert(song)
    }

    override suspend fun delete(song: FavoriteSong) {
        favoriteSongDao.delete(song)
    }
}