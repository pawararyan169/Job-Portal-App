package com.example.jobportal.model

// Data model for a single job listing displayed in the feed
data class Job(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val salaryRange: String, // e.g., "$80k - $100k"
    val descriptionSnippet: String, // Short summary for the list view
    val postDate: String, // e.g., "Posted 2 hours ago"
    val jobType: String // e.g., "Full-time", "Remote"
)

// Data class to wrap the list of jobs received from the API
data class JobFeedResponse(
    val success: Boolean,
    val jobs: List<Job>,
    val message: String?
)