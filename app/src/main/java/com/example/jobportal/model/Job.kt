package com.example.jobportal.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// This entity stores the ID of a job the user has marked as 'saved'.
@Entity(tableName = "saved_jobs")
data class SavedJob(
    // The actual job ID from the backend API
    @PrimaryKey
    val jobId: String,

    // The ID of the user who saved it (e.g., the user's email or MongoDB ID)
    val userId: String,

    // Timestamp for when it was saved (optional, but useful)
    val savedAt: Long = System.currentTimeMillis()
)