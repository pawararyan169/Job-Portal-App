package com.example.jobportal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobportal.api.RetrofitClient
import com.example.jobportal.model.Job
import com.example.jobportal.model.JobPost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class JobFeedState(
    val jobs: List<Job> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class JobFeedViewModel : ViewModel() {
    private val _state = MutableStateFlow(JobFeedState())
    val state: StateFlow<JobFeedState> = _state.asStateFlow()

    init {
        refreshJobs()
    }

    fun refreshJobs() {
        _state.value = _state.value.copy(isLoading = true, error = null)

        // --- FIX: Unresolved reference 'getJobFeed' ---
        RetrofitClient.apiService.getJobFeed().enqueue(object : Callback<List<Job>> {
            override fun onResponse(call: Call<List<Job>>, response: Response<List<Job>>) {
                if (response.isSuccessful) {
                    _state.value = _state.value.copy(
                        jobs = response.body() ?: emptyList(),
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Failed to load jobs: HTTP ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<List<Job>>, t: Throwable) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = t.localizedMessage ?: "Network error fetching jobs"
                )
            }
        })
    }

    // --- FIX: Unresolved reference 'postJob' (Used in RecruiterPostJobScreen) ---
    fun postJob(jobPost: JobPost, token: String, onResult: (Boolean, String) -> Unit) {
        val authHeader = "Bearer $token"

        RetrofitClient.apiService.postJob(jobPost, authHeader).enqueue(object : Callback<Job> {
            override fun onResponse(call: Call<Job>, response: Response<Job>) {
                if (response.isSuccessful && response.body() != null) {
                    refreshJobs() // Refresh feed after successful post
                    onResult(true, "Job posted successfully!")
                } else {
                    val message = response.errorBody()?.string() ?: "Failed to post job: HTTP ${response.code()}"
                    onResult(false, message)
                }
            }

            override fun onFailure(call: Call<Job>, t: Throwable) {
                onResult(false, t.localizedMessage ?: "Network error during job post.")
            }
        })
    }
}
