package com.example.jobportal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jobportal.ui.AuthViewModel
import com.example.jobportal.model.UserRegistrationRequest
import com.example.jobportal.model.UserRegistrationResponse
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.background

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isRecruiter by remember { mutableStateOf(false) }

    // State for password visibility toggles
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    // Professional vertical gradient background
    val gradientBackground = Brush.verticalGradient(
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
            Text("Create Account",
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold))
            Text("Join SkillSync now",
                style = MaterialTheme.typography.subtitle1.copy(color = Color.Gray))
            Spacer(modifier = Modifier.height(24.dp))

            // --- Elevated Card for Form Fields ---
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                backgroundColor = Color.White
            ) {
                Column(modifier = Modifier.padding(24.dp)) {

                    // Name Field
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp))
                    Spacer(modifier = Modifier.height(12.dp))

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
                    Spacer(modifier = Modifier.height(12.dp))

                    // --- Confirm Password Field with Show/Hide Toggle ---
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(imageVector = image, contentDescription = if (confirmPasswordVisible) "Hide confirm password" else "Show confirm password")
                            }
                        },
                        isError = password.isNotBlank() && password != confirmPassword,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Role Toggle
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isRecruiter, onCheckedChange = { isRecruiter = it })
                        Text("Sign up as Recruiter", style = MaterialTheme.typography.body1)
                    }
                }
            } // --- End Card ---

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (password != confirmPassword || name.isBlank() || email.isBlank() || password.isBlank()) {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Please fill all fields and ensure passwords match.")
                        }
                        return@Button
                    }

                    val request = UserRegistrationRequest(email, name, password, confirmPassword, isRecruiter)

                    viewModel.registerUser(request) { response: UserRegistrationResponse ->
                        if (response.success) {
                            onNavigateToLogin()
                            coroutineScope.launch { scaffoldState.snackbarHostState.showSnackbar("Registration successful! Please log in.") }
                        } else {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(response.message, actionLabel = "Dismiss")
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Sign Up", style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Bold))
            }

            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onNavigateToLogin) {
                Text("Already have an account? Log In")
            }
        }
    }
}
