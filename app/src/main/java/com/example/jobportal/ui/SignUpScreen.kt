package com.example.jobportal.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults // FIX: Added OutlinedTextFieldDefaults for M3 color access
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.jobportal.network.ApiService
import com.example.jobportal.network.User
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(onSignUpSuccess: () -> Unit, onNavigateToLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isRecruiter by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        // FIX: M2 colors.background -> M3 colorScheme.background
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Account",
                // FIX: M2 typography.h4 -> M3 typography.headlineMedium
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sign up to get started",
                // FIX: M2 typography.subtitle1 -> M3 typography.bodyLarge
                style = MaterialTheme.typography.bodyLarge,
                // FIX: M2 colors.onBackground -> M3 colorScheme.onBackground
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // FIX: OutlinedTextField is M3, must use M3 defaults for colors
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
                modifier = Modifier.fillMaxWidth(),
                // FIX: Use M3 OutlinedTextFieldDefaults.colors
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // FIX: OutlinedTextField is M3, must use M3 defaults for colors
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
                trailingIcon = {
                    val image = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                    }
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                // FIX: Use M3 OutlinedTextFieldDefaults.colors
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // FIX: OutlinedTextField is M3, must use M3 defaults for colors
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Confirm Password Icon") },
                trailingIcon = {
                    val image = if (confirmPasswordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                        Icon(imageVector = image, contentDescription = "Toggle Confirm Password Visibility")
                    }
                },
                visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                // FIX: Use M3 OutlinedTextFieldDefaults.colors
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Checkbox is M3
                Checkbox(checked = isRecruiter, onCheckedChange = { isRecruiter = it })
                Spacer(modifier = Modifier.width(8.dp))
                Text("Register as Recruiter")
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Button is M3
            Button(
                onClick = {
                    if (password != confirmPassword) {
                        error = "Passwords do not match"
                        return@Button
                    }
                    scope.launch {
                        try {
                            ApiService.create().signUp(User(email, password, isRecruiter))
                            onSignUpSuccess()
                        } catch (e: Exception) {
                            error = "Sign up failed: ${e.localizedMessage ?: e.message}"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                // FIX: M2 typography.button -> M3 typography.labelLarge
                Text("Sign Up", style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))

            error?.let {
                // FIX: M2 colors.error -> M3 colorScheme.error
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Text("Already have an account?")
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Login",
                    // FIX: M2 colors.primary -> M3 colorScheme.primary
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}
