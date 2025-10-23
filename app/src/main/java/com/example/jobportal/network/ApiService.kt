package com.example.jobportal.api

import com.example.jobportal.model.JobPost
import com.example.jobportal.model.UserLoginRequest
import com.example.jobportal.model.UserLoginResponse
import com.example.jobportal.model.UserRegistrationRequest
import com.example.jobportal.model.UserRegistrationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    // FIX: Removed leading slash. Appends cleanly: .../api/auth/signup
    @POST("signup")
    suspend fun registerUser(@Body request: UserRegistrationRequest): UserRegistrationResponse

    // Appends cleanly: .../api/auth/login
    @POST("login")
    suspend fun loginUser(@Body request: UserLoginRequest): UserLoginResponse

    // This route uses an absolute path for /api/jobs/post, assuming it's outside the /api/auth router.
    @POST("/api/jobs/post")
    suspend fun postJob(@Body jobPost: JobPost): JobPost
}