package com.example.jobportal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jobportal.model.JobPost
import kotlinx.coroutines.launch

// Assuming AuthViewModel holds the token logic
// NOTE: You must provide a valid AuthViewModel instance here if you rely on its token getter.
@Composable
fun RecruiterPostJobScreen(
    recruiterId: Int,
    jobFeedViewModel: JobFeedViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    onPostSuccess: () -> Unit = {}
) {
    var jobTitle by remember { mutableStateOf("") }
    var jobDescription by remember { mutableStateOf("") }
    var salaryRange by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val OceanBlue = Color(0xFF0077B6)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = { Text("Post New Job", color = Color.White) }, backgroundColor = OceanBlue)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = jobTitle,
                onValueChange = { jobTitle = it },
                label = { Text("Job Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = jobDescription,
                onValueChange = { jobDescription = it },
                label = { Text("Job Description") },
                modifier = Modifier.fillMaxWidth().height(200.dp),
                singleLine = false
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = salaryRange,
                onValueChange = { salaryRange = it },
                label = { Text("Salary Range (e.g., $80K - $100K)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (jobTitle.isBlank() || jobDescription.isBlank()) {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Please fill in all required fields.")
                        }
                        return@Button
                    }

                    val newJob = JobPost(
                        title = jobTitle,
                        description = jobDescription,
                        recruiterId = recruiterId // Use the ID passed to the screen
                    )

                    // --- FIX: Unresolved reference 'postJob' is now resolved ---
                    jobFeedViewModel.postJob(newJob, authViewModel.getAuthToken()) { success, message ->
                        coroutineScope.launch {
                            if (success) {
                                scaffoldState.snackbarHostState.showSnackbar(message)
                                onPostSuccess()
                            } else {
                                scaffoldState.snackbarHostState.showSnackbar("Error: $message", actionLabel = "Dismiss")
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Publish Job")
            }
        }
    }
}
