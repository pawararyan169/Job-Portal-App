
package com.example.jobportal
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.example.jobportal.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    var currentScreen by remember { mutableStateOf("Login") }
                    var isRecruiter by remember { mutableStateOf(false) }
                    var yearsOfExperience by remember { mutableStateOf(0) }

                    when (currentScreen) {
                        "Login" -> LoginScreen(
                            onLoginSuccess = { email, recruiter ->
                                isRecruiter = recruiter
                                currentScreen = if (recruiter) "RecruiterPostJob" else "ProfileSetup"
                            },
                            onNavigateToSignUp = { currentScreen = "SignUp" }
                        )
                        "SignUp" -> SignUpScreen(
                            onSignUpSuccess = { currentScreen = "Login" },
                            onNavigateToLogin = { currentScreen = "Login" }
                        )
                        "ProfileSetup" -> ProfileSetupScreen(
                            onProfileSaved = {
                                currentScreen = "JobSeekerJobList"
                            },
                            onBack = { currentScreen = "Login" },
                            onYearsOfExperienceSet = { years ->
                                yearsOfExperience = years
                            }
                        )
                        "RecruiterPostJob" -> RecruiterPostJobScreen(
                            onJobPosted = { /* Optionally show success */ }
                        )
                        "JobSeekerJobList" -> JobSeekerJobListScreen(
                            yearsOfExperience = yearsOfExperience
                        )
                    }
                }
            }
        }
    }
}