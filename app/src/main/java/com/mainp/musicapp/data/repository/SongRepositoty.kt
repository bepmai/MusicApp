package com.mainp.musicapp.data.repository

import com.mainp.musicapp.data.entity.Song

interface SongRepository {
    suspend fun fetchSongs(): List<Song>

    suspend fun fetchDirectSongUrl(songUrl: String): String?
}