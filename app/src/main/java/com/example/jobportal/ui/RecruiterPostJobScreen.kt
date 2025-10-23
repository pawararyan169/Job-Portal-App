package com.example.jobportal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jobportal.api.RetrofitClient // FIX: Corrected import
import com.example.jobportal.model.JobPost
import kotlinx.coroutines.launch

@Composable
fun RecruiterPostJobScreen(
    // Assuming you pass the Recruiter's ID to this screen
    recruiterId: Int
) {
    var jobTitle by remember { mutableStateOf("") }
    var jobDescription by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Post a New Job", style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = jobTitle, onValueChange = { jobTitle = it }, label = { Text("Job Title") })
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = jobDescription,
                onValueChange = { jobDescription = it },
                label = { Text("Description") },
                modifier = Modifier.height(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val jobPost = JobPost(
                    title = jobTitle,
                    description = jobDescription,
                    recruiterId = recruiterId // FIX: Providing the required 'id' parameter
                )

                coroutineScope.launch {
                    try {
                        // Resolved: Correctly calling postJob on the ApiService
                        val response = RetrofitClient.apiService.postJob(jobPost)
                        scaffoldState.snackbarHostState.showSnackbar("Job Posted: ${response.title}")
                    } catch (e: Exception) {
                        scaffoldState.snackbarHostState.showSnackbar("Failed to post job: ${e.message}")
                    }
                }
            }) {
                Text("Post Job")
            }
        }
    }
}