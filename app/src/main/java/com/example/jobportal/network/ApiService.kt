// ApiService.kt
package com.example.jobportal.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

data class Job(
    val _id: String? = null,
    val title: String,
    val company: String,
    val description: String,
    val location: String
)

data class User(
    val email: String,
    val password: String,
    val isRecruiter: Boolean? = null
)

data class LoginResponse(
    val email: String,
    val isRecruiter: Boolean
)

interface ApiService {
    @GET("jobs")
    suspend fun getJobs(): List<Job>

    @POST("jobs")
    suspend fun postJob(@Body job: Job): Job

    @POST("user/signup")
    suspend fun signUp(@Body user: User)

    @POST("user/login")
    suspend fun login(@Body user: User): LoginResponse

    companion object {
        private const val BASE_URL = "http://10.0.2.2:5000/"

        fun create(): ApiService {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}