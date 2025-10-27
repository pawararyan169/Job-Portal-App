package com.example.jobportal.network

import com.example.jobportal.model.Job
import com.example.jobportal.model.JobPost
import com.example.jobportal.model.ProfileUpdateRequest
import com.example.jobportal.model.UserLoginRequest
import com.example.jobportal.model.UserLoginResponse
import com.example.jobportal.model.UserRegistrationRequest
import com.example.jobportal.model.UserRegistrationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    // AUTH Endpoints
    @POST("auth/register")
    fun registerUser(@Body request: UserRegistrationRequest): Call<UserRegistrationResponse>

    @POST("auth/login")
    fun loginUser(@Body request: UserLoginRequest): Call<UserLoginResponse>

    // PROFILE Endpoints
    @PUT("profile/update")
    fun updateProfile(@Body request: ProfileUpdateRequest, @Header("Authorization") token: String): Call<UserRegistrationResponse>

    // JOB FEED Endpoints
    @GET("jobs/feed")
    fun getJobFeed(): Call<List<Job>>

    // RECRUITER Endpoints
    @POST("jobs/post")
    fun postJob(@Body jobPost: JobPost, @Header("Authorization") token: String): Call<Job>
}
