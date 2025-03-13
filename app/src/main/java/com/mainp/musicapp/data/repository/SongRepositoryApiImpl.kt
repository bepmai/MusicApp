package com.mainp.musicapp.data.repository

import android.util.Log
import com.mainp.musicapp.api.ApiService
import com.mainp.musicapp.data.entity.Song
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class SongRepositoryApiImpl(private val api: ApiService) : SongRepository {
    override suspend fun fetchSongs(): List<Song> {
        val response = api.getSongs()
        if (response.isSuccessful) {
            val songs = response.body() ?: emptyList()
            return songs
        } else {
            Log.e("API_ERROR", "Lỗi API: ${response.errorBody()?.string()}")
            return emptyList()
        }
    }

    override suspend fun fetchDirectSongUrl(songUrl: String): String? {
        return try {
            val requestBody = songUrl.toRequestBody("text/plain".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("link", null, requestBody)

            val response = ApiService.create().getSongLink(part)

            if (response.isSuccessful) {
                val html = response.body()?.success ?: return null

                val dataSrc = extractAudioSrc(html)

                dataSrc
            } else {
                Log.e("API_ERROR", "Phản hồi không thành công: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("API_EXCEPTION", "Lỗi khi lấy link nhạc: ${e.message}")
            null
        }
    }


    fun extractAudioSrc(html: String): String? {
        Log.d("EXTRACT_HTML", "HTML input: $html")

        val document: Document = Jsoup.parse(html)
        val audioSrc = document.select(".amazingaudioplayer-source").attr("data-src")

        Log.d("EXTRACTED_DATA_SRC", "Link audio: $audioSrc")
        return audioSrc
    }

}