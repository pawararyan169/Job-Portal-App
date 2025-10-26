package com.example.jobportal.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.example.jobportal.model.Job
import com.example.jobportal.model.JobFeedResponse

// Define data structure for Drawer Items
data class DrawerItem(val icon: ImageVector, val title: String, val route: String)

@Composable
fun JobSeekerHomeScreen(
    userEmail: String, // REQUIRED PARAMETER 1 (The compiler was looking for this name)
    onNavigate: (String) -> Unit, // REQUIRED PARAMETER 2
    onViewJobDetails: (String) -> Unit, // REQUIRED PARAMETER 3
    onLogout: () -> Unit, // REQUIRED PARAMETER 4
    viewModel: JobFeedViewModel = viewModel()
) {
    val drawerItems = listOf(
        DrawerItem(Icons.Default.Search, "Find Jobs", "jobs_feed"),
        DrawerItem(Icons.Default.BookmarkBorder, "Saved Jobs", "saved_jobs"),
        DrawerItem(Icons.Default.WorkOutline, "My Applications", "my_applications"),
        DrawerItem(Icons.Default.PersonOutline, "Edit Profile", "edit_profile"),
        DrawerItem(Icons.Default.Settings, "Settings", "settings"),
        DrawerItem(Icons.Default.HelpOutline, "Help & Support", "help")
    )

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val jobFeedState by viewModel.state.collectAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Job Finder", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            )
        },
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(250.dp)
                    .padding(16.dp)
            ) {
                Text("Job Seeker Menu", style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold))
                Text(userEmail, style = MaterialTheme.typography.subtitle2, color = Color.Gray)
                Divider(modifier = Modifier.padding(vertical = 16.dp))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(drawerItems) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    scope.launch { scaffoldState.drawerState.close() }
                                    onNavigate(item.route)
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(item.icon, contentDescription = item.title)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(item.title, style = MaterialTheme.typography.body1)
                        }
                    }
                }

                Divider()
                TextButton(onClick = { onLogout() }, modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout", tint = Color.Red)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Logout", color = Color.Red, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        },
        content = { padding ->
            JobFeedContent(
                state = jobFeedState,
                modifier = Modifier.padding(padding).fillMaxSize(),
                onRefresh = viewModel::refreshJobs,
                onJobClick = onViewJobDetails
            )
        }
    )
}

@Composable
fun JobFeedContent(state: JobFeedState, modifier: Modifier, onRefresh: () -> Unit, onJobClick: (String) -> Unit) {
    if (state.isLoading) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (state.error != null) {
        Column(
            modifier = modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Error loading jobs: ${state.error}", color = Color.Red, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRefresh) {
                Text("Try Again")
            }
        }
    } else if (state.jobs.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("No jobs found matching your criteria.")
        }
    } else {
        // Display the list of jobs
        LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
            item {
                Text("Showing ${state.jobs.size} jobs available", style = MaterialTheme.typography.subtitle2, color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
            }
            items(state.jobs) { job ->
                JobCard(job = job, onJobClick = onJobClick)
                Divider()
            }
        }
    }
}

@Composable
fun JobCard(job: Job, onJobClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onJobClick(job.id) },
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(job.title, style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold, color = MaterialTheme.colors.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text("${job.company} - ${job.location}", style = MaterialTheme.typography.subtitle1)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Localized Currency
                Text("${job.jobType} | ₹${job.salaryRange.replace("$", "")}", style = MaterialTheme.typography.body2, color = Color.Gray)
                Text(job.postDate, style = MaterialTheme.typography.caption, color = Color.DarkGray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(job.descriptionSnippet, maxLines = 3, style = MaterialTheme.typography.body2)
        }
    }
}