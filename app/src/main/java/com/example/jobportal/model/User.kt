package com.example.jobportal.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = false)
    val email: String,
    val name: String?,
    val photoUrl: String?
)

// Minimal required models for communication
data class JobPost(
    val id: Int = 0,
    val title: String,
    val description: String,
    val recruiterId: Int
)

data class UserRegistrationRequest(
    val email: String,
    val name: String,
    val password: String,
    val confirmPassword: String,
    val isRecruiter: Boolean = false
)

data class UserRegistrationResponse(
    val success: Boolean,
    val message: String
)

data class UserLoginRequest(
    val email: String,
    val password: String
)

// FIX: Added the 'message' field so the LoginScreen can access response.message
data class UserLoginResponse(
    val success: Boolean,
    val token: String?,
    val userType: String?,
    val message: String? // <-- NEW FIELD
)
