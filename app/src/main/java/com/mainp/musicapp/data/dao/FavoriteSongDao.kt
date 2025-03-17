package com.mainp.musicapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mainp.musicapp.data.entity.FavoriteSong
import com.mainp.musicapp.data.entity.Song

@Dao
interface FavoriteSongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(song: FavoriteSong)

    @Delete
    suspend fun delete(song: FavoriteSong)

    @Query("SELECT * FROM favorite_songs")
    fun getAllFavoriteSong(): LiveData<List<FavoriteSong>>

    @Query("SELECT * FROM favorite_songs WHERE id = :songId")
    suspend fun getById(songId: String): FavoriteSong?
}