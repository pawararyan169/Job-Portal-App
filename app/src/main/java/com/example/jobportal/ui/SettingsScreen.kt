package com.example.jobportal.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector

// Define settings categories
data class SettingItem(val title: String, val subtitle: String, val icon: ImageVector, val route: String)

@Composable
fun SettingsScreen(
    userEmail: String, // CRITICAL FIX: The parameter the NavGraph needs to pass
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    val accountSettings = listOf(
        SettingItem("My Profile", "View and edit personal details", Icons.Default.PersonOutline, "edit_profile"),
        SettingItem("My Applications", "View history of job applications", Icons.Default.History, "my_applications"),
    )

    val appSettings = listOf(
        SettingItem("Location Services", "Allow app to access your location", Icons.Default.LocationOn, "geolocation"),
        SettingItem("Download Data", "Download a copy of your application history", Icons.Default.Download, "download_data")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ChevronLeft, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        // Main list is scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // --- HEADER ---
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PersonOutline, contentDescription = "User", modifier = Modifier.size(40.dp), tint = MaterialTheme.colors.primary)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Account Settings", style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold))
                    Text(userEmail, style = MaterialTheme.typography.body2, color = Color.Gray)
                }
            }
            Divider()

            // --- USER SETTINGS ---
            SettingsSection(title = "Profile & History") {
                accountSettings.forEach { item ->
                    SettingsRow(item = item, onClick = { onNavigate(item.route) })
                }
            }

            // --- APP SETTINGS ---
            SettingsSection(title = "App Preferences") {
                appSettings.forEach { item ->
                    SettingsRow(item = item, onClick = { onNavigate(item.route) })
                }
            }

            // --- LOGOUT ---
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            TextButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.ExitToApp, contentDescription = "Logout", tint = Color.Red)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Sign Out", color = Color.Red, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)) {
        Text(title, style = MaterialTheme.typography.subtitle1, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
fun SettingsRow(item: SettingItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(item.icon, contentDescription = item.title, tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(item.title, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Medium)
                Text(item.subtitle, style = MaterialTheme.typography.caption, color = Color.Gray)
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
    }
}