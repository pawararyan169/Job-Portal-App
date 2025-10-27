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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.jobportal.ui.JobSeekerJobListScreen
import com.example.jobportal.ui.RecruiterSignUpScreen
import com.example.jobportal.ui.RecruiterProfileSetupScreen // <-- All components are imported here

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object RecruiterSignUp : Screen("recruiter_signup")
    data object SetupProfile : Screen("setup_profile/{userId}/{userEmail}/{authToken}")
    data object RecruiterProfileSetup : Screen("recruiter_profile_setup/{userId}/{userEmail}") {
        fun createRoute(userId: String, userEmail: String) = "recruiter_profile_setup/$userId/$userEmail"
    }
    data object RecruiterDashboard : Screen("recruiter_dashboard")
    data object EditProfile : Screen("edit_profile")
    data object Settings : Screen("settings")
    data object JobSearch : Screen("job_search")
    data object JobDetail : Screen("job_detail/{jobId}") {
        fun createRoute(jobId: String) = "job_detail/$jobId"
    }
    data object PostJob : Screen("post_job/{recruiterId}") {
        fun createRoute(recruiterId: Int) = "post_job/$recruiterId"
    }
    fun createSetupProfileRoute(userId: String, userEmail: String, authToken: String) = "setup_profile/$userId/$userEmail/$authToken"
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

    val tokenForStartup = "STATIC_DEBUG_TOKEN"

    val finalStartDestination = when {
        userEmail == null -> Screen.Login.route
        userRole == "recruiter" -> Screen.RecruiterDashboard.route
        currentUserId != null && !isProfileComplete -> Screen.SetupProfile.createSetupProfileRoute(currentUserId, userEmail, tokenForStartup)
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
                onLoginSuccess = { userId, userEmail, token ->
                    navController.navigate(Screen.SetupProfile.createSetupProfileRoute(userId, userEmail, token)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
            )
        }

        // --- Sign Up Screen (Job Seeker) ---
        composable(Screen.SignUp.route) {
            SignUpScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToRecruiterSignUp = { navController.navigate(Screen.RecruiterSignUp.route) }
            )
        }

        // --- Recruiter Sign Up Screen ---
        composable(Screen.RecruiterSignUp.route) {
            RecruiterSignUpScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onSignUpSuccess = { userId, userEmail ->
                    val debugToken = "STATIC_DEBUG_TOKEN"
                    navController.navigate(Screen.RecruiterProfileSetup.createRoute(userId, userEmail)) {
                        popUpTo(Screen.RecruiterSignUp.route) { inclusive = true }
                    }
                },
                onBackToJobSeekerSignUp = { navController.popBackStack() }
            )
        }

        // --- Job Seeker Profile Setup Screen ---
        composable(
            route = Screen.SetupProfile.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("userEmail") { type = NavType.StringType },
                navArgument("authToken") { type = NavType.StringType }
            )
        ) { backStackEntry: NavBackStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            val email = backStackEntry.arguments?.getString("userEmail")
            val token = backStackEntry.arguments?.getString("authToken")

            if (userId != null && email != null && token != null) {
                ProfileSetupScreen(
                    userId = userId,
                    userEmail = email,
                    authToken = token,
                    onProfileSaved = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.SetupProfile.route) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: User context or authentication token not found for profile setup.")
                }
            }
        }

        // --- Recruiter Profile Setup Screen ---
        composable(
            route = Screen.RecruiterProfileSetup.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("userEmail") { type = NavType.StringType }
            )
        ) { backStackEntry: NavBackStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            val email = backStackEntry.arguments?.getString("userEmail")
            val token = authViewModel.getAuthToken()

            if (userId != null && email != null) {
                RecruiterProfileSetupScreen(
                    userId = userId,
                    userEmail = email,
                    authToken = token,
                    onProfileSaved = {
                        navController.navigate(Screen.RecruiterDashboard.route) {
                            popUpTo(Screen.RecruiterProfileSetup.route) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: User context not found for recruiter profile setup.")
                }
            }
        }

        // --- Home Screen (Job Seeker Hub) ---
        composable(Screen.Home.route) {
            val userEmailToUse = userEmail ?: "Guest"

            JobSeekerHomeScreen(
                userEmail = userEmailToUse,
                onNavigate = { route ->
                    when (route) {
                        "edit_profile" -> navController.navigate(Screen.SetupProfile.createSetupProfileRoute(currentUserId ?: "0", userEmailToUse, tokenForStartup))
                        "settings" -> navController.navigate(Screen.Settings.route)
                        "jobs_feed" -> navController.navigate(Screen.JobSearch.route)
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

        // --- Job Search Screen ---
        composable(Screen.JobSearch.route) {
            JobSeekerJobListScreen(
                onBack = { navController.popBackStack() },
                onJobClick = { jobId ->
                    navController.navigate(Screen.JobDetail.createRoute(jobId))
                }
            )
        }

        // --- Job Detail Screen ---
        composable(
            route = Screen.JobDetail.route,
            arguments = listOf(navArgument("jobId") { type = NavType.StringType })
        ) { backStackEntry: NavBackStackEntry ->
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
                },
                onBack = { navController.popBackStack() }
            )
        }

        // --- Post Job Screen ---
        composable(
            route = Screen.PostJob.route,
            arguments = listOf(navArgument("recruiterId") { type = NavType.IntType })
        ) { backStackEntry: NavBackStackEntry ->
            val recruiterId = backStackEntry.arguments?.getString("recruiterId")?.toIntOrNull() ?: 0
            RecruiterPostJobScreen(recruiterId = recruiterId)
        }
    }
}
