package com.example.jobportal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jobportal.model.ProfileUpdateRequest
import kotlinx.coroutines.launch

// ❌ DUPLICATE CODE REMOVED: The sealed class Screen and fun AppNavGraph were deleted from here.

@Composable
fun ProfileSetupScreen(
    userId: String,
    userEmail: String,
    authToken: String, // Required for the API call
    onProfileSaved: () -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var fullName by remember { mutableStateOf("") }
    var headline by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var highestEducation by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val OceanBlue = Color(0xFF0077B6)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Complete Your Profile", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                backgroundColor = OceanBlue
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Personal & Professional Details",
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "We need a little more information, $userEmail.",
                style = MaterialTheme.typography.subtitle1,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(32.dp))

            // --- Form Fields ---

            OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Full Name *") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = headline, onValueChange = { headline = it }, label = { Text("Headline * (e.g., Senior Kotlin Developer)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = summary, onValueChange = { summary = it }, label = { Text("Summary / Bio") }, modifier = Modifier.fillMaxWidth().height(100.dp), singleLine = false)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City / Location *") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = highestEducation, onValueChange = { highestEducation = it }, label = { Text("Highest Education") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(32.dp))


            // --- Save Button ---
            Button(
                onClick = {
                    if (fullName.isBlank() || headline.isBlank() || city.isBlank()) {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Please fill in required fields: Name, Headline, and City.")
                        }
                        return@Button
                    }

                    val request = ProfileUpdateRequest(
                        userId = userId,
                        fullName = fullName,
                        headline = headline,
                        summary = summary,
                        phone = phone,
                        city = city,
                        highestEducation = highestEducation
                    )

                    // Call the ViewModel function with the token received via navigation
                    viewModel.updateProfile(request, authToken) { success, message ->
                        coroutineScope.launch {
                            if (success) {
                                scaffoldState.snackbarHostState.showSnackbar(message)
                                onProfileSaved()
                            } else {
                                scaffoldState.snackbarHostState.showSnackbar("Error: $message", actionLabel = "Dismiss")
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = OceanBlue)
            ) {
                Text("Save Profile and Continue", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
