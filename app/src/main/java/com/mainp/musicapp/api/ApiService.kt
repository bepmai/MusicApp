package com.mainp.musicapp.api

import com.mainp.musicapp.data.entity.Song
import com.mainp.musicapp.data.entity.SongResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @GET("api.php?hotsong")
    suspend fun getSongs(): Response<List<Song>>


    @Multipart
    @POST("api.php")
    suspend fun getSongLink(
        @Part link: MultipartBody.Part
    ): Response<SongResponse>

    companion object {
        fun create(): ApiService {
            return RetrofitInstance.retrofit.create(ApiService::class.java)
        }
    }
}
