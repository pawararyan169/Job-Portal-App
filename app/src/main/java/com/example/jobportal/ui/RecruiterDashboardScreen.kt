package com.example.jobportal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Placeholder data structure for a metric card
data class Metric(val title: String, val value: String, val color: Color)

// Placeholder data structure for a recent application
data class RecentApplication(val applicantName: String, val jobTitle: String, val status: String)

@Composable
fun RecruiterDashboardScreen(
    recruiterName: String,
    onViewReports: () -> Unit,
    onPostNewJob: () -> Unit
) {
    val metrics = listOf(
        Metric("Total Job Posts", "18", Color(0xFF673AB7)),
        Metric("Active Applications", "45", Color(0xFF03A9F4)),
        Metric("Interviews Scheduled", "5", Color(0xFFFF9800))
    )

    val recentApplications = listOf(
        RecentApplication("Dhaval Palan", "Senior Kotlin Dev", "New"),
        RecentApplication("Vaishali Joshi", "UX/UI Designer", "Pending"),
        RecentApplication("Payal Bhanushali", "Backend Engineer", "Interview")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recruiter Dashboard", fontWeight = FontWeight.Bold) },
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
            Text("Welcome, $recruiterName", style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(24.dp))

            // --- 1. Key Metrics Overview (Row of Cards) ---
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                metrics.forEach { metric ->
                    MetricCard(metric = metric, modifier = Modifier.weight(1f).padding(horizontal = 4.dp))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // --- 2. Analytics / Insights Card ---
            Text("Hiring Insights", style = MaterialTheme.typography.h6, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                backgroundColor = Color(0xFF212121)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("Top Skills Required (Chart Placeholder)", color = Color.LightGray)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // --- 3. Recent Activity List ---
            Text("Recent Applications", style = MaterialTheme.typography.h6, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            recentApplications.forEach { app ->
                RecentApplicationRow(app = app)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // --- Post New Job Button ---
            Button(
                onClick = onPostNewJob,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Post New Job", style = MaterialTheme.typography.button)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// --- Composable for Metric Card ---
@Composable
fun MetricCard(metric: Metric, modifier: Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        backgroundColor = metric.color,
        contentColor = Color.White,
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(metric.value, style = MaterialTheme.typography.h5, fontWeight = FontWeight.ExtraBold)
            Text(metric.title, style = MaterialTheme.typography.caption)
        }
    }
}

// --- Composable for Recent Application Row ---
@Composable
fun RecentApplicationRow(app: RecentApplication) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = 1.dp,
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(app.applicantName, style = MaterialTheme.typography.subtitle1, fontWeight = FontWeight.SemiBold)
                Text("Applied for: ${app.jobTitle}", style = MaterialTheme.typography.caption)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(app.status, style = MaterialTheme.typography.body2, color = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Filled.ChevronRight, contentDescription = "View Details")
            }
        }
    }
}