package com.example.meditation.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    const val baseURL = "http://192.168.223.206/meditation/public/api/Users"
    fun getRetrofit() : Retrofit {
        return Retrofit.Builder().baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun getService() : ApiService{
        return getRetrofit().create(ApiService::class.java)
        }
}