package com.mainp.musicapp.api

import com.mainp.musicapp.data.entity.Song
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("api.php?hotsong")
    suspend fun getSongs(): Response<List<Song>>

    companion object {
        fun create(): ApiService {
            return RetrofitInstance.retrofit.create(ApiService::class.java)
        }
    }
}