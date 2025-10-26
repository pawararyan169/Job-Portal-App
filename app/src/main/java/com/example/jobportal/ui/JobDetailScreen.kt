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
import com.example.jobportal.model.Job
import com.example.jobportal.model.JobFeedResponse
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// NOTE: In a final app, this screen would use a JobDetailViewModel to fetch
// the full job description from the API using the provided jobId.

@Composable
fun JobDetailScreen(
    jobId: String,
    onBack: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    // Placeholder data state for job details (simulate API response)
    val jobDetails by remember {
        mutableStateOf(
            Job(
                id = jobId,
                title = "Senior Kotlin Developer",
                company = "TechInnovate Solutions",
                location = "Remote, India",
                salaryRange = "₹12,00,000 - ₹15,00,000",
                descriptionSnippet = "Full job description loading...",
                postDate = "Posted 2 hours ago",
                jobType = "Full-time"
            )
        )
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(jobDetails.title, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(jobDetails.company, style = MaterialTheme.typography.h5, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(jobDetails.location, style = MaterialTheme.typography.subtitle1, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            // Details Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Salary:", fontWeight = FontWeight.Medium)
                Text(jobDetails.salaryRange, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Type:", fontWeight = FontWeight.Medium)
                Text(jobDetails.jobType)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Job Description", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
            Divider()
            Spacer(modifier = Modifier.height(8.dp))
            // Placeholder for the full description
            Text("This is where the full, detailed job description will be loaded from the API using the Job ID. This usually includes responsibilities, qualifications, and company culture details.",
                fontSize = 14.sp)

            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {
                    scope.launch { scaffoldState.snackbarHostState.showSnackbar("Apply Button Clicked!") }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Apply Now")
            }
        }
    }
}

