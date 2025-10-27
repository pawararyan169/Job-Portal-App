package com.example.jobportal.ui

import androidx.lifecycle.ViewModel
import com.example.jobportal.api.RetrofitClient
import com.example.jobportal.model.ProfileUpdateRequest
import com.example.jobportal.model.UserLoginRequest
import com.example.jobportal.model.UserLoginResponse
import com.example.jobportal.model.UserRegistrationRequest
import com.example.jobportal.model.UserRegistrationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Placeholder for internal user state management (if needed)
data class UserState(
    val isAuthenticated: Boolean = false,
    val userId: String? = null,
    val userEmail: String? = null,
    val userType: String? = null,
    val authToken: String? = null,
    val isLoading: Boolean = false
)

class AuthViewModel : ViewModel() {

    // --- Registration Logic (Used by SignUpScreen) ---
    fun registerUser(request: UserRegistrationRequest, onResult: (UserRegistrationResponse) -> Unit) {
        RetrofitClient.apiService.registerUser(request).enqueue(object : Callback<UserRegistrationResponse> {
            override fun onResponse(call: Call<UserRegistrationResponse>, response: Response<UserRegistrationResponse>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    onResult(body)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Registration failed (HTTP ${response.code()})"
                    onResult(UserRegistrationResponse(success = false, message = errorMessage))
                }
            }

            override fun onFailure(call: Call<UserRegistrationResponse>, t: Throwable) {
                onResult(UserRegistrationResponse(success = false, message = t.localizedMessage ?: "Network error"))
            }
        })
    }

    // --- Login Logic (Used by LoginScreen) ---
    fun loginUser(request: UserLoginRequest, onResult: (UserLoginResponse) -> Unit) {
        RetrofitClient.apiService.loginUser(request).enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                val body = response.body()
                if (response.isSuccessful && body != null && body.success) {
                    onResult(body)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Login failed (HTTP ${response.code()})"
                    onResult(UserLoginResponse(success = false, token = null, userType = null, message = errorMessage, userId = null))
                }
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                onResult(UserLoginResponse(success = false, token = null, userType = null, message = t.localizedMessage ?: "Network error", userId = null))
            }
        })
    }

    // --- Profile Update Logic (Used by ProfileSetupScreen) ---
    fun updateProfile(profileRequest: ProfileUpdateRequest, token: String, onResult: (Boolean, String) -> Unit) {
        val authHeader = "Bearer $token"
        RetrofitClient.apiService.updateProfile(profileRequest, authHeader).enqueue(object : Callback<UserRegistrationResponse> {
            override fun onResponse(call: Call<UserRegistrationResponse>, response: Response<UserRegistrationResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    onResult(true, "Profile updated successfully!")
                } else {
                    val message = response.body()?.message ?: response.errorBody()?.string() ?: "Profile update failed."
                    onResult(false, message)
                }
            }

            override fun onFailure(call: Call<UserRegistrationResponse>, t: Throwable) {
                onResult(false, t.localizedMessage ?: "Network error during profile update.")
            }
        })
    }

    // Placeholder token getter (required for API calls)
    fun getAuthToken(): String {
        // NOTE: In a real app, retrieve the token from SharedPreferences or SecureStore
        return "DEBUG_TOKEN"
    }
}
