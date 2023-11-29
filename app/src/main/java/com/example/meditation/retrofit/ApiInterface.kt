package com.example.meditation.retrofit

import com.example.meditation.model.SignInBody
import com.example.meditation.model.Users
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @Headers("Content-Type:application/json")
    @POST("Users")
    fun signin(@Body info: SignInBody): retrofit2.Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @POST("Users")
    fun registerUser(
        @Body info: Users
    ): retrofit2.Call<ResponseBody>


}