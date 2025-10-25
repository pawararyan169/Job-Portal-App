package com.example.jobportal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobportal.api.RetrofitClient
// FIX: Explicitly import ALL models used
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
                onResult(UserRegistrationResponse(true, response.message))
            } catch (e: Exception) {
                onResult(UserRegistrationResponse(false, "Registration failed: ${e.message}"))
            }
        }
    }

    fun loginUser(request: UserLoginRequest, onResult: (UserLoginResponse) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.loginUser(request)
                onResult(response)
            } catch (e: Exception) {
                // FIX: Constructs the failure response with all required parameters
                onResult(
                    UserLoginResponse(
                        success = false,
                        token = null,
                        userType = null,
                        message = "Login failed due to network or server error.",
                        userId = null
                    )
                )
            }
        }
    }
}