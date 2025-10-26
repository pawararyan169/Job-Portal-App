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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.jobportal.model.ProfileUpdateRequest
import com.example.jobportal.api.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun ProfileSetupScreen(
    userId: String,
    onProfileSaved: () -> Unit,
    onBack: () -> Unit
) {

    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var yearsOfExperience by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var highestEducation by remember { mutableStateOf("") }

    // State variables for file uploads
    var profilePhotoUri by remember { mutableStateOf<Uri?>(null) }
    var resumeUri by remember { mutableStateOf<Uri?>(null) }

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val apiService = remember { RetrofitClient.apiService }

    // Theme Colors from SignUpScreen
    val OceanBlue = Color(0xFF0077B6)

    // Custom TextField Colors matching SignUpScreen aesthetic
    val customTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = OceanBlue,
        unfocusedBorderColor = Color.LightGray,
        cursorColor = OceanBlue,
        textColor = Color.Black,
        backgroundColor = Color.White
    )


    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF8EC5FC), Color(0xFFE0C340)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    // --- File Pickers ---
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
                .imePadding() // Adjusts for the keyboard
        ) {
            // --- Animated and Colorful Background Shapes (Copied from SignUpScreen) ---
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
                        size = androidx.compose.ui.geometry.Size(canvasWidth * 0.4f, canvasHeight * 0.2f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState), // Makes the entire content scrollable
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top // Changed to Top for profile screen
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Text("Complete Profile",
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold, color = Color.Black))
                Text("Tell us about yourself to find the best jobs.",
                    style = MaterialTheme.typography.subtitle1.copy(color = Color.DarkGray))
                Spacer(modifier = Modifier.height(32.dp))


                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = Color.White.copy(alpha = 0.95f)
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                        // PROFILE PHOTO UPLOAD (Card style matching the aesthetic)
                        Card(
                            modifier = Modifier
                                .size(100.dp)
                                .clickable { photoPickerLauncher.launch("image/*") },
                            shape = RoundedCornerShape(50.dp),
                            elevation = 4.dp,
                            backgroundColor = OceanBlue // Use accent color for empty state
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
                                    Icon(Icons.Filled.PhotoCamera, contentDescription = "Add Photo", tint = Color.White, modifier = Modifier.size(40.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))


                        OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Full Name") },
                            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Name Icon", tint = OceanBlue) },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), colors = customTextFieldColors)
                        Spacer(modifier = Modifier.height(12.dp))


                        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") },
                            leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = "Phone Icon", tint = OceanBlue) },
                            modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            shape = RoundedCornerShape(8.dp), colors = customTextFieldColors)
                        Spacer(modifier = Modifier.height(12.dp))

                        // City Field
                        OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("Current City") },
                            leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = "City Icon", tint = OceanBlue) },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), colors = customTextFieldColors)
                        Spacer(modifier = Modifier.height(12.dp))


                        OutlinedTextField(value = highestEducation, onValueChange = { highestEducation = it }, label = { Text("Highest Education") },
                            leadingIcon = { Icon(Icons.Filled.School, contentDescription = "Education Icon", tint = OceanBlue) },
                            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), colors = customTextFieldColors)
                        Spacer(modifier = Modifier.height(12.dp))


                        OutlinedTextField(
                            value = yearsOfExperience,
                            onValueChange = { yearsOfExperience = it.filter { char -> char.isDigit() } },
                            label = { Text("Years of Experience") },
                            leadingIcon = { Icon(Icons.Filled.Star, contentDescription = "Experience Icon", tint = OceanBlue) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(8.dp), colors = customTextFieldColors
                        )
                        Spacer(modifier = Modifier.height(12.dp))


                        OutlinedTextField(value = skills, onValueChange = { skills = it }, label = { Text("Skills (Headline/Summary)") },
                            leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = "Skills Icon", tint = OceanBlue) },
                            modifier = Modifier.fillMaxWidth(), minLines = 3, shape = RoundedCornerShape(8.dp), colors = customTextFieldColors)
                        Spacer(modifier = Modifier.height(24.dp))


                        Button(
                            onClick = { resumePickerLauncher.launch("application/pdf") },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEEEEEE), contentColor = OceanBlue)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.FileUpload, contentDescription = "Upload Resume", modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    if (resumeUri != null) "Resume Selected: READY" else "Upload Resume (PDF)",
                                    fontWeight = FontWeight.SemiBold,
                                    color = OceanBlue
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp)) // Padding before button


                Button(
                    onClick = {
                        val years = yearsOfExperience.toIntOrNull()

                        if (fullName.isBlank() || years == null || resumeUri == null) {
                            coroutineScope.launch { scaffoldState.snackbarHostState.showSnackbar("Please fill all required fields and upload your CV/Resume.") }
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
                                // In a real app, you would handle file upload here (profilePhotoUri, resumeUri)
                                onProfileSaved()
                            } catch (e: Exception) {
                                scaffoldState.snackbarHostState.showSnackbar("Profile update failed: ${e.message}", actionLabel = "Dismiss")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = OceanBlue)
                ) {
                    Text("Save and Continue", style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Bold, color = Color.White))
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onBack) {
                    Text("Back to Dashboard", color = OceanBlue)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}