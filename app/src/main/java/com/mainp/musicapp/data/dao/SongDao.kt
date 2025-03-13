package com.mainp.musicapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mainp.musicapp.data.entity.Song

@Dao
interface SongDao {
    @Insert
    suspend fun insert(song: Song)

    @Query("SELECT * FROM song_tbt")
    suspend fun getAllSongs(): List<Song>
}