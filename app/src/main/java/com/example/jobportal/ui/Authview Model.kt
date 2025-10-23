package com.example.jobportal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobportal.api.RetrofitClient
import com.example.jobportal.model.UserLoginRequest
import com.example.jobportal.model.UserLoginResponse
import com.example.jobportal.model.UserRegistrationRequest
import com.example.jobportal.model.UserRegistrationResponse
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    fun registerUser(request: UserRegistrationRequest, onResult: (UserRegistrationResponse) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.registerUser(request)
                // Assuming successful response includes success:true and a message
                onResult(UserRegistrationResponse(true, response.message))
            } catch (e: Exception) {
                // Return a failure response with a specific error message
                onResult(UserRegistrationResponse(false, "Registration failed: ${e.message}"))
            }
        }
    }

    fun loginUser(request: UserLoginRequest, onResult: (UserLoginResponse) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.loginUser(request)
                // Pass the successful response object directly
                onResult(response)
            } catch (e: Exception) {
                // FIX: Added the 'message' parameter with a default error value.
                onResult(
                    UserLoginResponse(
                        success = false,
                        token = null,
                        userType = null,
                        message = "Login failed due to network or server error."
                    )
                )
            }
        }
    }
}