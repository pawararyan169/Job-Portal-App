package com.example.jobportal.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate

import com.example.jobportal.model.ProfileUpdateRequest
import com.example.jobportal.network.ApiService
import com.example.jobportal.api.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun ProfileSetupScreen(
    userId: String,
    userEmail: String,
    onProfileSaved: () -> Unit,
    onBack: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(userEmail) }
    var phone by remember { mutableStateOf("") }
    var yearsOfExperience by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var highestEducation by remember { mutableStateOf("") }

    var profilePhotoUri by remember { mutableStateOf<Uri?>(null) }
    var resumeUri by remember { mutableStateOf<Uri?>(null) }

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val apiService = remember { RetrofitClient.apiService }

    val scrollState = rememberScrollState()

    // Define the focus/accent color (Ocean Blue)
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

    // Dynamic gradient background (Matching Login/SignUp)
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF8EC5FC), Color(0xFFE0C340)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profilePhotoUri = uri
    }

    val resumePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        resumeUri = uri
    }

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
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .imePadding() // Handles keyboard overlap
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Complete Your Profile",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                Text(
                    "Mandatory fields required to start applying for jobs.",
                    style = MaterialTheme.typography.subtitle1,
                    color = Color.DarkGray // Changed to DarkGray for contrast on light background elements
                )
                Spacer(modifier = Modifier.height(24.dp))

                // --- SCROLLABLE FORM CONTENT WRAPPER ---
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {

                    // PROFILE PHOTO UPLOAD
                    Card(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.CenterHorizontally)
                            .clickable { photoPickerLauncher.launch("image/*") },
                        shape = RoundedCornerShape(50.dp),
                        elevation = 4.dp,
                        backgroundColor = Color.White // Set card background to white
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            if (profilePhotoUri != null) {
                                AsyncImage(
                                    model = profilePhotoUri,
                                    contentDescription = "Profile Photo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Text("Add Photo", style = MaterialTheme.typography.caption, color = Color.Gray)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    // --- Input Fields (Wrapped in a single Card for uniform background) ---
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        shape = RoundedCornerShape(12.dp),
                        backgroundColor = Color.White
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), colors = customTextFieldColors)
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), enabled = false, colors = customTextFieldColors)
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), colors = customTextFieldColors)
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("Current City") }, modifier = Modifier.fillMaxWidth(), colors = customTextFieldColors)
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(value = highestEducation, onValueChange = { highestEducation = it }, label = { Text("Highest Education") }, modifier = Modifier.fillMaxWidth(), colors = customTextFieldColors)
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = yearsOfExperience,
                                onValueChange = { yearsOfExperience = it.filter { char -> char.isDigit() } },
                                label = { Text("Years of Experience") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = customTextFieldColors
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(value = skills, onValueChange = { skills = it }, label = { Text("Skills (Headline)") }, modifier = Modifier.fillMaxWidth(), minLines = 3, colors = customTextFieldColors)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    // RESUME UPLOAD BUTTON (Outside the inner card, aligned with the main column)
                    Button(
                        onClick = { resumePickerLauncher.launch("application/pdf") },
                        modifier = Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEEEEEE), contentColor = Color.Black)
                    ) {
                        Text(if (resumeUri != null) "Resume Selected" else "Upload Resume (PDF)", fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                // --- END SCROLLABLE CONTENT ---

                // --- Submit Button (Fixed at Bottom) ---
                Button(
                    onClick = {
                        val years = yearsOfExperience.toIntOrNull()

                        if (fullName.isBlank() || email.isBlank() || years == null || resumeUri == null) {
                            coroutineScope.launch { scaffoldState.snackbarHostState.showSnackbar("Please fill all fields and upload your CV/Resume.") }
                            return@Button
                        }

                        val request = ProfileUpdateRequest(
                            userId = userId,
                            fullName = fullName,
                            headline = skills,
                            summary = skills,
                            phone = phone,
                            city = city,
                            highestEducation = highestEducation
                        )

                        coroutineScope.launch {
                            try {
                                val response = apiService.updateProfile(request)
                                onProfileSaved()
                            } catch (e: Exception) {
                                scaffoldState.snackbarHostState.showSnackbar("Profile update failed: ${e.message}. Check backend console.")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = OceanBlue)
                ) {
                    Text("Save and Continue", fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Back",
                    modifier = Modifier.clickable { onBack() },
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp)) // Final bottom space
            }
        }
    }
}