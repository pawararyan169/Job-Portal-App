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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector

// --- Reusable Social Buttons Composable ---
@Composable
fun SocialSignInButtons(onGoogleSignIn: () -> Unit, onGitHubSignIn: () -> Unit) {
    val GoogleRed = Color(0xFFDB4437)
    val GitHubBlack = Color(0xFF333333)
    val buttonHeight = 48.dp

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        // 1. Google Sign In Button
        Button(
            onClick = onGoogleSignIn,
            modifier = Modifier.fillMaxWidth().height(buttonHeight),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = GoogleRed, contentColor = Color.White),
            elevation = ButtonDefaults.elevation(defaultElevation = 2.dp)
        ) {
            Icon(Icons.Filled.Star, contentDescription = "Google Logo", modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text("Sign in with Google", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. GitHub Sign In Button
        Button(
            onClick = onGitHubSignIn,
            modifier = Modifier.fillMaxWidth().height(buttonHeight),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = GitHubBlack, contentColor = Color.White),
            elevation = ButtonDefaults.elevation(defaultElevation = 2.dp)
        ) {
            Icon(Icons.Filled.AccountCircle, contentDescription = "GitHub Logo", modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text("Sign in with GitHub", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: (userId: String, userEmail: String) -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    // Define the accent color (Ocean Blue)
    val OceanBlue = Color(0xFF0077B6)

    // Define custom colors for text fields
    val customTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = OceanBlue,
        unfocusedBorderColor = Color.LightGray,
        cursorColor = OceanBlue,
        textColor = Color.Black,
        backgroundColor = Color.White,
        focusedLabelColor = OceanBlue,
        unfocusedLabelColor = Color.DarkGray
    )

    // Dynamic gradient background (Light Blue to Yellow/Orange)
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

                drawCircle(color = Color.White.copy(alpha = 0.1f), radius = canvasWidth * 0.4f, center = Offset(canvasWidth * 0.1f, canvasHeight * 0.2f))
                drawCircle(color = Color(0xFFFFCC80).copy(alpha = 0.2f), radius = canvasWidth * 0.25f, center = Offset(canvasWidth * 0.8f, canvasHeight * 0.7f))
                rotate(degrees = 45f, pivot = Offset(canvasWidth * 0.5f, canvasHeight * 0.5f)) {
                    drawRect(color = Color(0xFF80DEEA).copy(alpha = 0.15f), topLeft = Offset(canvasWidth * 0.3f, canvasHeight * 0.1f), size = androidx.compose.ui.geometry.Size(canvasWidth * 0.4f, canvasHeight * 0.2f))
                }
            }
            // --- End Background Shapes ---

            // --- Content Column (Scrollable Fix) ---
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
                Text("Sign In to Job Portal",
                    style = MaterialTheme.typography.subtitle1.copy(color = Color.DarkGray))
                Spacer(modifier = Modifier.height(32.dp))

                // --- USERNAME/PASSWORD FORM CARD ---
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

                        // Password Field
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(imageVector = image, contentDescription = "Show/Hide password", tint = OceanBlue)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp),
                            colors = customTextFieldColors
                        )
                    }
                } // --- End Card ---

                Spacer(modifier = Modifier.height(32.dp))

                // --- Email/Password Login Button ---
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
                                val userId = response.userId
                                if (userId != null) {
                                    onLoginSuccess(userId, email)
                                    coroutineScope.launch { scaffoldState.snackbarHostState.showSnackbar("Login successful!") }
                                } else {
                                    coroutineScope.launch { scaffoldState.snackbarHostState.showSnackbar("Login successful, but missing user data.", actionLabel = "Error") }
                                }

                            } else {
                                val message = response.message ?: "Login failed. Check credentials."
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(message = message, actionLabel = "Dismiss")
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = OceanBlue)
                ) {
                    Text("Sign In", style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Bold, color = Color.White))
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- Divider (Or) ---
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                    Text(" OR ", modifier = Modifier.padding(horizontal = 8.dp), color = Color.Gray)
                    Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- SOCIAL SIGN IN BUTTONS ---
                SocialSignInButtons(
                    onGoogleSignIn = { /* TODO: Implement Google Sign-In logic */ },
                    onGitHubSignIn = { /* TODO: Implement GitHub Sign-In logic */ }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- SIGN UP NAVIGATION LINK ---
                TextButton(onClick = onNavigateToSignUp) {
                    Text("Don't have an account? Sign Up", color = OceanBlue, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(32.dp)) // Extra space for bottom safety
            }
        }
    }
}