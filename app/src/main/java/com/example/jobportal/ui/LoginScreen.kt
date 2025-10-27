package com.example.jobportal.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.foundation.background
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jobportal.model.UserLoginRequest
import com.example.jobportal.model.UserLoginResponse
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    // FIX: Updated signature to match NavGraph requirement (userId, userEmail, token)
    onLoginSuccess: (userId: String, userEmail: String, token: String) -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

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

    // Dynamic gradient background (Matches Sign Up Screen)
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF8EC5FC), Color(0xFFE0C340)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    Scaffold(scaffoldState = scaffoldState) { padding ->
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Welcome Back",
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold, color = Color.Black))
                Text("Log in to SkillSync",
                    style = MaterialTheme.typography.subtitle1.copy(color = Color.DarkGray))
                Spacer(modifier = Modifier.height(32.dp))

                // --- FORM CARD ---
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = Color.White.copy(alpha = 0.95f)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {

                        // Email Field
                        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp),
                            colors = customTextFieldColors
                        )
                        Spacer(modifier = Modifier.height(16.dp))

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
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- Login Button ---
                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Please enter email and password.")
                            }
                            return@Button
                        }

                        val request = UserLoginRequest(email, password)

                        viewModel.loginUser(request) { response: UserLoginResponse ->
                            if (response.success && response.userId != null && response.token != null) {
                                coroutineScope.launch { scaffoldState.snackbarHostState.showSnackbar("Login successful!") }
                                // FIX: Pass the required three arguments
                                onLoginSuccess(response.userId, email, response.token)
                            } else {
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(response.message ?: "Login failed.", actionLabel = "Dismiss")
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = OceanBlue)
                ) {
                    Text("Log In", style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Bold, color = Color.White))
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onNavigateToSignUp) {
                    Text("Don't have an account? Sign Up", color = OceanBlue)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
