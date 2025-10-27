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

// This screen name likely caused conflict and should be verified if it's still needed.
// If it's the intended search screen, ensure JobSeekerJobListScreen uses the appropriate
// ViewModel and State types, which should be available due to the shared package structure.
@Composable
fun JobSearchScreen(
    onBack: () -> Unit,
    onJobClick: (String) -> Unit,
    viewModel: JobFeedViewModel = viewModel()
) {
    // ... (Your original JobSearchScreen implementation)
}

// CRITICAL: This is the single, authoritative definition of the JobCard for the whole project.
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