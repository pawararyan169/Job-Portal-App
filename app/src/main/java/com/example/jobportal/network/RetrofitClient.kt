package com.example.jobportal.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import com.example.jobportal.network.ApiService

object RetrofitClient {
    // Port 5000 is used because your running Node.js server confirmed it is listening on that port.
    // 10.0.2.2 is the special IP for the emulator to access the host machine.
    private const val BASE_URL = "http://10.0.2.2:5000/api/"

    // Custom HTTP client with longer timeout (30s) to avoid connection errors during long operations
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
