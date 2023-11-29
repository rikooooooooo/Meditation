package com.example.meditation.api

import retrofit2.Call
import retrofit2.http.GET
import com.example.meditation.response.Response

interface ApiService {
    @GET("users")
    fun getMorty () : Call<Response>
}