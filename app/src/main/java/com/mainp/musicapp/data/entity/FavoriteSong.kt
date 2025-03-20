package com.mainp.musicapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_songs")
data class FavoriteSong(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val thumbnail: String,
    val path: String,
    val duration: Int
): Serializable