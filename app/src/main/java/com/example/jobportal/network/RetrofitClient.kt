package com.example.jobportal.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import com.example.jobportal.network.ApiService

object RetrofitClient {
    // FINAL STABLE IP: 10.0.2.2 is the address the Emulator routes traffic to.
    private const val BASE_URL = "http://10.0.2.2:5000/api/"

    // Custom HTTP client with longer timeout (30s) for stability
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}