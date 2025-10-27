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
import com.example.jobportal.model.JobFeedResponse // Assuming JobFeedResponse and JobFeedState are defined elsewhere

// Define data structure for Drawer Items
data class DrawerItem(val icon: ImageVector, val title: String, val route: String)

@Composable
fun JobSeekerHomeScreen(
    userEmail: String,
    onNavigate: (String) -> Unit,
    onViewJobDetails: (String) -> Unit,
    onLogout: () -> Unit,
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

// Assuming JobFeedState is defined elsewhere
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
                // Calls the JobCard defined in JobSearchScreen.kt
                JobCard(job = job, onJobClick = onJobClick)
                Divider()
            }
        }
    }
}
// JobCard definition is correctly omitted here.