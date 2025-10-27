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

// --- JOB/PROFILE MODELS ---
data class JobPost(
    val id: Int = 0,
    val title: String,
    val description: String,
    val recruiterId: Int
)

data class ProfileUpdateRequest(
    val userId: String,
    val fullName: String,
    val headline: String,
    val summary: String,
    val phone: String,
    val city: String,
    val highestEducation: String
)

data class JobSeekerProfile(
    val userId: String,
    val fullName: String,
    val headline: String,
    val summary: String,
    val phone: String,
    val city: String,
    val highestEducation: String,
    val isComplete: Boolean = true
)



// --- AUTH MODELS ---
data class UserRegistrationRequest(
    val email: String,
    val name: String,
    val password: String,
    val confirmPassword: String,
    val isRecruiter: Boolean = false
)

data class UserRegistrationResponse(
    val success: Boolean,
    val message: String,
    // FIX: ADDED MISSING FIELDS to match the backend response
    val userId: String? = null,
    val userEmail: String? = null
)

// Dummy/Other Data Models (for compilation context)

// ... other models (Job, ProfileUpdateRequest, etc.)




data class UserLoginRequest(
    val email: String,
    val password: String
)

data class UserLoginResponse(
    val success: Boolean,
    val token: String?,
    val userType: String?,
    val message: String?,
    val userId: String?
)

data class Job(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val salaryRange: String,
    val descriptionSnippet: String,
    val postDate: String,
    val jobType: String
)

data class JobFeedResponse(
    val success: Boolean,
    val jobs: List<Job>,
    val message: String?
)