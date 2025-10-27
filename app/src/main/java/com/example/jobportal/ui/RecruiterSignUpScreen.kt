package com.example.jobportal.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
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
fun RecruiterSignUpScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onSignUpSuccess: (userId: String, userEmail: String) -> Unit, // Callback to navigate to profile setup
    onBackToJobSeekerSignUp: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") } // Recruiter-specific field
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val OceanBlue = Color(0xFF0077B6)
    val customTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = OceanBlue,
        unfocusedBorderColor = Color.LightGray,
        cursorColor = OceanBlue,
        textColor = Color.Black,
        backgroundColor = Color.White
    )
    // Reusing the same gradient background from LoginScreen
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF8EC5FC), Color(0xFFE0C340)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Recruiter Sign Up", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackToJobSeekerSignUp) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back to Job Seeker Sign Up", tint = Color.White)
                    }
                },
                backgroundColor = OceanBlue,
                contentColor = Color.White
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(padding)
        ) {
            // --- Animated and Colorful Background Shapes (Reused) ---
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
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
                        size = androidx.compose.ui.geometry.Size(canvasWidth * 0.4f, canvasWidth * 0.2f)
                    )
                }
            }

            // --- Content Column (Scrollable) ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text("Recruiter Sign Up",
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold, color = Color.Black))
                Text("Post Jobs and Find Talent",
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

                        // Name Field
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Your Name") },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp),
                            colors = customTextFieldColors
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Company Name Field (Recruiter Specific)
                        OutlinedTextField(value = companyName, onValueChange = { companyName = it }, label = { Text("Company Name (Required)") },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp),
                            colors = customTextFieldColors
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Email Field
                        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Company Email") },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp),
                            colors = customTextFieldColors
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // --- Password Field ---
                        OutlinedTextField(
                            value = password, onValueChange = { password = it }, label = { Text("Password") },
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
                            value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password") },
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

                        // Navigation link back to Job Seeker Sign Up
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable { onBackToJobSeekerSignUp() },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Go back", tint = OceanBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Switch to Job Seeker Sign Up",
                                style = MaterialTheme.typography.body1.copy(color = OceanBlue, fontWeight = FontWeight.SemiBold))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- Sign Up Button ---
                Button(
                    onClick = {
                        if (password != confirmPassword || name.isBlank() || email.isBlank() || password.isBlank() || companyName.isBlank()) {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Please fill all fields, including Company Name, and ensure passwords match.")
                            }
                            return@Button
                        }

                        val request = UserRegistrationRequest(email, name, password, confirmPassword, true)

                        viewModel.registerUser(request) { response: UserRegistrationResponse ->
                            if (response.success) {
                                // These fields are available because UserRegistrationResponse was fixed to include them.
                                val newUserId = response.userId ?: "REC-DEFAULT"
                                val newEmail = response.userEmail ?: email

                                coroutineScope.launch { scaffoldState.snackbarHostState.showSnackbar("Recruiter registration successful!") }
                                onSignUpSuccess(newUserId, newEmail) // NAVIGATE TO PROFILE SETUP
                            } else {
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(response.message, actionLabel = "Dismiss")
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = OceanBlue)
                ) {
                    Text("Sign Up (Recruiter)", style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Bold, color = Color.White))
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onNavigateToLogin) {
                    Text("Already have an account? Log In", color = OceanBlue)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}


