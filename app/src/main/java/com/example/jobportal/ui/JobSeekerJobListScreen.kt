package com.example.jobportal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.jobportal.network.ApiService
import com.example.jobportal.network.Job

@Composable
fun JobSeekerJobListScreen(yearsOfExperience: Int) {
    var jobs by remember { mutableStateOf(listOf<Job>()) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedJobTitle by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // In a real app, you might want to show a loading state here
        try {
            jobs = ApiService.create().getJobs()
        } catch (e: Exception) {
            // Handle error, e.g., showing a snackbar or error message
            println("Error fetching jobs: ${e.message}")
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "Available Jobs",
            // Material 3 equivalent of h5 is headlineSmall
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Job List (LazyColumn for performance)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(jobs) { job ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) { // Increased padding for better look
                        Text(
                            job.title,
                            // Material 3 equivalent of h6 is titleLarge
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            job.company,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            job.location,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            job.description,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            selectedJobTitle = job.title
                            showDialog = true
                        }) {
                            Text("Apply")
                        }
                    }
                }
            }
        }
    }

    // Application Confirmation Dialog (Material 3)
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Application Confirmation") },
            text = {
                Text(
                    "Your application for \"$selectedJobTitle\" has been submitted. Your years of experience ($yearsOfExperience) will be considered during the review process."
                )
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
