package com.example.jobportal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jobportal.ui.AuthViewModel
import com.example.jobportal.model.UserLoginRequest
import com.example.jobportal.model.UserLoginResponse
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush // Keep Brush import

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit, // Callback for navigation upon success
    onNavigateToSignUp: () -> Unit // Callback to navigate to sign up
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    // FIX: Switched to Brush.verticalGradient which correctly handles Alignment properties.
    val gradientBackground = Brush.verticalGradient(
        // Using light colors for a professional feel
        colors = listOf(Color(0xFFE0F7FA), Color(0xFFFFFFFF)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    Scaffold(scaffoldState = scaffoldState) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground) // Apply the gradient background
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Welcome Back",
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold))
            Text("Sign In to SkillSync",
                style = MaterialTheme.typography.subtitle1.copy(color = Color.Gray))
            Spacer(modifier = Modifier.height(32.dp))

            // --- Elevated Card for Form Fields ---
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                backgroundColor = Color.White
            ) {
                Column(modifier = Modifier.padding(24.dp)) {

                    // Email Field
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp))
                    Spacer(modifier = Modifier.height(12.dp))

                    // --- Password Field with Show/Hide Toggle ---
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)
                    )
                }
            } // --- End Card ---

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Please enter your email and password.")
                        }
                        return@Button
                    }

                    val request = UserLoginRequest(email, password)

                    viewModel.loginUser(request) { response: UserLoginResponse ->
                        if (response.success) {
                            // TODO: Store token/user data securely here before navigating
                            onLoginSuccess()
                            coroutineScope.launch { scaffoldState.snackbarHostState.showSnackbar("Login successful!") }
                        } else {
                            val message = response.message ?: "Login failed. Check credentials."
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = message,
                                    actionLabel = "Dismiss"
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Sign In", style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Bold))
            }

            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onNavigateToSignUp) {
                Text("Don't have an account? Sign Up")
            }
        }
    }
}
