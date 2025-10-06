package com.example.jobportal.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ProfileSetupScreen(
    onProfileSaved: () -> Unit,
    onBack: () -> Unit,
    onYearsOfExperienceSet: (Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var yearsOfExperience by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    // Removed val scope = rememberCoroutineScope() as it is not used for async API calls here.

    Surface(
        modifier = Modifier.fillMaxSize(),
        // FIX: Material 2's 'colors.background' changed to Material 3's 'colorScheme.background'
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Set Up Your Profile",
                // FIX: Material 2's 'typography.h5' changed to Material 3's 'headlineSmall'
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = yearsOfExperience,
                onValueChange = { yearsOfExperience = it },
                label = { Text("Years of Experience") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = skills, onValueChange = { skills = it }, label = { Text("Skills (comma separated)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val years = yearsOfExperience.toIntOrNull()
                    if (name.isBlank() || email.isBlank() || years == null) {
                        error = "Please fill all required fields with valid data"
                        return@Button
                    }
                    onYearsOfExperienceSet(years)
                    // Here you may call backend API to save profile
                    onProfileSaved()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                // FIX: Used explicit import for RoundedCornerShape
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Profile")
            }
            Spacer(modifier = Modifier.height(16.dp))

            error?.let {
                // FIX: Material 2's 'colors.error' changed to Material 3's 'colorScheme.error'
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Back",
                modifier = Modifier.clickable { onBack() },
                // FIX: Material 2's 'colors.primary' changed to Material 3's 'colorScheme.primary'
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}