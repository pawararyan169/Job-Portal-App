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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
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

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    // Define the focus/accent color (Ocean Blue)
    val OceanBlue = Color(0xFF0077B6)

    // Define custom colors for text fields
    val customTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = OceanBlue,
        unfocusedBorderColor = Color.LightGray,
        cursorColor = OceanBlue,
        textColor = Color.Black,
        backgroundColor = Color.White
    )

    // Dynamic gradient background (Matching LoginScreen)
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF8EC5FC), Color(0xFFE0C340)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    Scaffold(scaffoldState = scaffoldState) { padding ->
        // Use a Box to layer the background gradient and the shapes
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(padding)
        ) {
            // --- Animated and Colorful Background Shapes ---
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Draw translucent circles and shapes
                drawCircle(
                    color = Color.White.copy(alpha = 0.1f),
                    radius = canvasWidth * 0.4f,
                    center = Offset(canvasWidth * 0.1f, canvasHeight * 0.2f)
                )

                drawCircle(
                    color = Color(0xFFFFCC80).copy(alpha = 0.2f),
                    radius = canvasWidth * 0.25f,
                    center = Offset(canvasWidth * 0.8f, canvasHeight * 0.7f)
                )

                rotate(degrees = 45f, pivot = Offset(canvasWidth * 0.5f, canvasHeight * 0.5f)) {
                    drawRect(
                        color = Color(0xFF80DEEA).copy(alpha = 0.15f),
                        topLeft = Offset(canvasWidth * 0.3f, canvasHeight * 0.1f),
                        size = androidx.compose.ui.geometry.Size(canvasWidth * 0.4f, canvasHeight * 0.2f)
                    )
                }
            }
            // --- End Background Shapes ---

            // --- Content Column (Scrollable Fix) ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState), // CRITICAL: Makes the entire content scrollable
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Create Account",
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold, color = Color.Black))
                Text("Join the Job Portal",
                    style = MaterialTheme.typography.subtitle1.copy(color = Color.DarkGray))
                Spacer(modifier = Modifier.height(32.dp))

                // --- FORM CARD ---
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = Color.White.copy(alpha = 0.95f) // Slightly translucent card
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {

                        // Name Field
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp),
                            colors = customTextFieldColors
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Email Field
                        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp),
                            colors = customTextFieldColors
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // --- Password Field ---
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
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp),
                            colors = customTextFieldColors
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // --- Confirm Password Field ---
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
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp),
                            colors = customTextFieldColors
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Role Toggle
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = isRecruiter, onCheckedChange = { isRecruiter = it },
                                colors = CheckboxDefaults.colors(checkedColor = OceanBlue) // Use the new focus color for checkbox
                            )
                            Text("Sign up as Recruiter", style = MaterialTheme.typography.body1)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp)) // Padding before button

                // --- Fixed Bottom Buttons ---
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
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = OceanBlue) // Use the new focus color for button
                ) {
                    Text("Sign Up", style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Bold, color = Color.White))
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onNavigateToLogin) {
                    Text("Already have an account? Log In", color = OceanBlue) // Apply color to text button
                }
                Spacer(modifier = Modifier.height(32.dp)) // Extra space for bottom safety
            } // --- End Content Column
        }
    }
}