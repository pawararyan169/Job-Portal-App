package com.example.jobportal.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jobportal.model.Job
import kotlinx.coroutines.launch

// ❌ FIX: The duplicate data class JobFeedState is REMOVED from this file.

@Composable
fun JobSeekerJobListScreen(
    onBack: () -> Unit,
    onJobClick: (String) -> Unit,
    viewModel: JobFeedViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val jobFeedState by viewModel.state.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    // Filter jobs based on search query
    val filteredJobs = remember(jobFeedState.jobs, searchQuery) {
        if (searchQuery.isBlank()) {
            jobFeedState.jobs
        } else {
            jobFeedState.jobs.filter { job ->
                job.title.contains(searchQuery, ignoreCase = true) ||
                        job.company.contains(searchQuery, ignoreCase = true) ||
                        job.location.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Find Jobs", fontWeight = FontWeight.Bold) },
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
                .padding(padding)
                .fillMaxSize()
        ) {
            // --- Persistent Search Bar ---
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by Title, Company, or Location") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear search")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true
            )
            Divider()

            // --- Job Feed Content Area ---
            if (jobFeedState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (jobFeedState.error != null) {
                // Display network/API error
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Error: ${jobFeedState.error}", color = Color.Red, fontWeight = FontWeight.Bold)
                    Button(onClick = viewModel::refreshJobs) { Text("Try Again") }
                }
            } else if (filteredJobs.isEmpty() && searchQuery.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No results found for \"$searchQuery\".")
                }
            } else {
                // Display the list of filtered jobs (reusing JobCard)
                LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)) {
                    item {
                        Text("${filteredJobs.size} matching jobs found.",
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.padding(vertical = 8.dp))
                    }
                    items(filteredJobs) { job ->
                        JobCard(job = job, onJobClick = onJobClick)
                        Divider()
                    }
                }
            }
        }
    }
}