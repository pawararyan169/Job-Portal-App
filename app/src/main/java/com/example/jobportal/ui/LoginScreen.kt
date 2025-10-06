package com.example.jobportal.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*           
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.jobportal.network.ApiService
import com.example.jobportal.network.User
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: (String, Boolean) -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var passwordVisibility by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Use Material 3 Surface and MaterialTheme
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome Back",
                // Use Material 3 typography
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Login to your account",
                // Use Material 3 typography and color scheme
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // OutlinedTextField from Material 3
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
                modifier = Modifier.fillMaxWidth(),
                // Use Material 3 OutlinedTextFieldDefaults
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // OutlinedTextField from Material 3
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
                trailingIcon = {
                    val image = if (passwordVisibility)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                    }
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                // Use Material 3 OutlinedTextFieldDefaults
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Material 3 Button
            Button(
                onClick = {
                    scope.launch {
                        try {
                            val response = ApiService.create().login(User(email, password))
                            onLoginSuccess(response.email, response.isRecruiter)
                        } catch (e: Exception) {
                            // Only set the error state
                            error = "Login failed: ${e.localizedMessage ?: e.message}"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                // Use Material 3 typography
                Text(text = "Login", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            error?.let {
                // Use Material 3 colorScheme
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Text("Don't have an account?")
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sign Up",
                    // Use Material 3 colorScheme
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToSignUp() }
                )
            }
        }
    }
}