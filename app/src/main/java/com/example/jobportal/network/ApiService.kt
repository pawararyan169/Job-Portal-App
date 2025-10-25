package com.example.jobportal.network

// FIX: Explicitly import ALL models from the correct 'model' package
import com.example.jobportal.model.JobPost
import com.example.jobportal.model.JobSeekerProfile
import com.example.jobportal.model.JobFeedResponse
import com.example.jobportal.model.ProfileUpdateRequest
import com.example.jobportal.model.UserLoginRequest
import com.example.jobportal.model.UserLoginResponse
import com.example.jobportal.model.UserRegistrationRequest
import com.example.jobportal.model.UserRegistrationResponse

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET

interface ApiService {

    @POST("auth/signup")
    suspend fun registerUser(@Body request: UserRegistrationRequest): UserRegistrationResponse

    @POST("auth/login")
    suspend fun loginUser(@Body request: UserLoginRequest): UserLoginResponse

    @GET("jobs/feed")
    suspend fun getJobFeed(): JobFeedResponse

    @POST("profile/complete")
    suspend fun updateProfile(@Body request: ProfileUpdateRequest): JobSeekerProfile

    @POST("jobs/post")
    suspend fun postJob(@Body jobPost: JobPost): JobPost
}