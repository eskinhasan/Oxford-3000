package com.example.oxford3000.service

import com.example.oxford3000.model.ApiResponse
import com.example.oxford3000.util.Words.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query



interface ImageApi {/*
    @GET("/api/")
    suspend fun imageSearch(
        @Query("q") searchQuery:String,
        @Query("key") apikey:String=API_KEY

    ): Call<List<ImageResult>>
    */
@GET("?key=$API_KEY")
suspend fun searchImages(@Query("q") query: String?):Response<ApiResponse>
}