package com.example.jobportal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobportal.api.RetrofitClient
import com.example.jobportal.model.Job
import com.example.jobportal.model.JobFeedResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Define the different states for the Job Feed UI
data class JobFeedState(
    val jobs: List<Job> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class JobFeedViewModel : ViewModel() {
    private val _state = MutableStateFlow(JobFeedState(isLoading = true))
    val state: StateFlow<JobFeedState> = _state

    init {
        fetchJobs()
    }

    private fun fetchJobs() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                // Call the API endpoint
                val response: JobFeedResponse = RetrofitClient.apiService.getJobFeed()

                if (response.success) {
                    _state.value = _state.value.copy(
                        jobs = response.jobs,
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(
                        error = response.message ?: "Failed to load jobs.",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Network Error: Could not connect or data parsing failed.",
                    isLoading = false
                )
            }
        }
    }

    // Function to reload data
    fun refreshJobs() {
        fetchJobs()
    }
}