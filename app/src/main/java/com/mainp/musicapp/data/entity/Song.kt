package com.mainp.musicapp.data.entity

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "song_tbt")
data class Song (
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("artists_names") val artist: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("link") val path: String,
    @SerializedName("duration") val duration: Int
): Serializable