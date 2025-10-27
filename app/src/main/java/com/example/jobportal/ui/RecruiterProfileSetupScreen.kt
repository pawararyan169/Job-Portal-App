package com.example.jobportal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.launch

@Composable
fun RecruiterProfileSetupScreen(
    userId: String,
    userEmail: String,
    authToken: String, // FIX: Added missing token parameter to align with navigation flow
    onProfileSaved: () -> Unit,
    onBack: () -> Unit
) {
    var companyBio by remember { mutableStateOf("") }
    var industry by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val OceanBlue = Color(0xFF0077B6)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Complete Recruiter Profile", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                backgroundColor = OceanBlue,
                contentColor = Color.White
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
                text = "Final Step: Company Details",
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This information ($userEmail) is required for posting jobs.",
                style = MaterialTheme.typography.subtitle1,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(32.dp))

            // --- Form Fields ---

            OutlinedTextField(
                value = companyBio,
                onValueChange = { companyBio = it },
                label = { Text("Company Bio / Description *") },
                placeholder = { Text("e.g., We are a leading tech startup...") },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                singleLine = false
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = industry,
                onValueChange = { industry = it },
                label = { Text("Primary Industry *") },
                placeholder = { Text("e.g., Software Development, Finance, Healthcare") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = website,
                onValueChange = { website = it },
                label = { Text("Company Website (Optional)") },
                placeholder = { Text("https://www.company.com") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(48.dp))

            // --- Save Button ---
            Button(
                onClick = {
                    if (companyBio.isBlank() || industry.isBlank()) {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Please fill in the required fields (Bio and Industry).")
                        }
                        return@Button
                    }

                    // In a real application, you would make an API call here to save data using the userId and authToken.
                    // e.g., viewModel.saveRecruiterProfile(userId, companyBio, industry, website, authToken)

                    // Simulate success and navigate to dashboard
                    onProfileSaved()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = OceanBlue)
            ) {
                Text("Complete Profile and Go to Dashboard", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
