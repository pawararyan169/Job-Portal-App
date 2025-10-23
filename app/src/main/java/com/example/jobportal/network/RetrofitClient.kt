package com.example.jobportal.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // FINAL CONFIGURATION: Base URL ends in / and includes the router prefix.
    private const val BASE_URL = "http://192.168.1.23:5000/api/auth/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}