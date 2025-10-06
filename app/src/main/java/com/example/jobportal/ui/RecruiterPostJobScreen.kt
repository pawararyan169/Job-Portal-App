package com.example.jobportal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.jobportal.network.ApiService
import com.example.jobportal.network.Job
import kotlinx.coroutines.launch

@Composable
fun RecruiterPostJobScreen(onJobPosted: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var requirements by remember { mutableStateOf("") }
    var salaryRange by remember { mutableStateOf("") }
    var experienceRequired by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Use Material 3 Typography (h5 -> headlineSmall) and FontWeight
        Text(
            "Post a New Job",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold // Use the correct imported FontWeight
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Job Title") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = company, onValueChange = { company = it }, label = { Text("Company Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Job Description") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = requirements,
            onValueChange = { requirements = it },
            label = { Text("Requirements") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = salaryRange, onValueChange = { salaryRange = it }, label = { Text("Salary Range") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = experienceRequired,
            onValueChange = { experienceRequired = it },
            label = { Text("Experience Required (years)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (title.isBlank() || company.isBlank() || description.isBlank() || experienceRequired.isBlank()) {
                    error = "Please fill all required fields"
                    return@Button
                }
                scope.launch {
                    try {
                        val api = ApiService.create()
                        val job = Job(
                            title = title,
                            company = company,
                            location = location,
                            description = """
                                $description
                                Requirements: $requirements
                                Salary: $salaryRange
                                Experience Required: $experienceRequired years
                            """.trimIndent() // Use trimIndent for cleaner multi-line strings
                        )
                        api.postJob(job)
                        onJobPosted()
                    } catch (e: Exception) {
                        error = "Failed to post job: ${e.localizedMessage ?: e.message}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Post Job")
        }
        Spacer(modifier = Modifier.height(12.dp))
        // Use Material 3 ColorScheme for error
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}