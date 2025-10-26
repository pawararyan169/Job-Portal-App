package com.example.jobportal.navigation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jobportal.ui.AuthViewModel
import com.example.jobportal.ui.LoginScreen
import com.example.jobportal.ui.SignUpScreen
import com.example.jobportal.ui.RecruiterPostJobScreen
import com.example.jobportal.ui.JobSeekerHomeScreen
import com.example.jobportal.ui.ProfileSetupScreen
import com.example.jobportal.ui.RecruiterDashboardScreen
import com.example.jobportal.ui.SettingsScreen
import com.example.jobportal.ui.JobDetailScreen
import com.example.jobportal.model.ProfileUpdateRequest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object SetupProfile : Screen("setup_profile/{userId}/{userEmail}")
    data object RecruiterDashboard : Screen("recruiter_dashboard")
    data object EditProfile : Screen("edit_profile")
    data object Settings : Screen("settings")
    data object JobDetail : Screen("job_detail/{jobId}") {
        fun createRoute(jobId: String) = "job_detail/$jobId"
    }
    data object PostJob : Screen("post_job/{recruiterId}") {
        fun createRoute(recruiterId: Int) = "post_job/$recruiterId"
    }
    fun createSetupProfileRoute(userId: String, userEmail: String) = "setup_profile/$userId/{userEmail}"
}

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    userEmail: String?,
    currentUserId: String? = null,
    isProfileComplete: Boolean = true,
    userRole: String? = null
) {
    val authViewModel: AuthViewModel = viewModel()

    val finalStartDestination = when {
        userEmail == null -> Screen.Login.route
        userRole == "recruiter" -> Screen.RecruiterDashboard.route
        currentUserId != null && !isProfileComplete -> Screen.SetupProfile.createSetupProfileRoute(currentUserId, userEmail)
        else -> Screen.Home.route
    }

    NavHost(
        navController = navController,
        startDestination = finalStartDestination,
        modifier = modifier
    ) {

        // --- Login Screen ---
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { userId, userEmail ->
                    navController.navigate(Screen.SetupProfile.createSetupProfileRoute(userId, userEmail)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
            )
        }

        // --- Sign Up Screen ---
        composable(Screen.SignUp.route) {
            SignUpScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.Login.route) }
            )
        }

        // --- Profile Setup Screen ---
        composable(Screen.SetupProfile.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            val email = backStackEntry.arguments?.getString("userEmail")

            if (userId != null && email != null) {
                ProfileSetupScreen(
                    userId = userId,
                    userEmail = email,
                    onProfileSaved = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.SetupProfile.route) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: User context not found for profile setup.")
                }
            }
        }

        // --- Home Screen (Job Seeker Hub) ---
        composable(Screen.Home.route) {
            val userEmailToUse = userEmail ?: "Guest"

            // FIX: Explicitly naming all arguments to avoid positional mismatch
            JobSeekerHomeScreen(
                userEmail = userEmailToUse, // FIX: Passing the userEmail parameter
                onNavigate = { route ->
                    when (route) {
                        "edit_profile" -> navController.navigate(Screen.SetupProfile.createSetupProfileRoute(currentUserId ?: "0", userEmailToUse))
                        "settings" -> navController.navigate(Screen.Settings.route)
                        else -> navController.navigate(route)
                    }
                },
                onViewJobDetails = { jobId ->
                    navController.navigate(Screen.JobDetail.createRoute(jobId))
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // --- Job Detail Screen (New Destination) ---
        composable(Screen.JobDetail.route) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId")

            if (jobId != null) {
                JobDetailScreen(
                    jobId = jobId,
                    onBack = { navController.popBackStack() }
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: Job details not found.")
                }
            }
        }

        // --- Recruiter Dashboard Screen ---
        composable(Screen.RecruiterDashboard.route) {
            RecruiterDashboardScreen(
                recruiterName = userEmail ?: "Recruiter",
                onViewReports = { /* navigate to reports */ },
                onPostNewJob = {
                    navController.navigate(Screen.PostJob.createRoute(currentUserId?.toIntOrNull() ?: 0))
                }
            )
        }

        // --- Settings Screen ---
        composable(Screen.Settings.route) {
            SettingsScreen(
                userEmail = userEmail ?: "Guest",
                onNavigate = { route -> navController.navigate(route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Settings.route) { inclusive = true }
                    }
                }
            )
        }

        // --- Post Job Screen ---
        composable(Screen.PostJob.route) { backStackEntry ->
            val recruiterId = backStackEntry.arguments?.getString("recruiterId")?.toIntOrNull() ?: 0
            RecruiterPostJobScreen(recruiterId = recruiterId)
        }
    }
}