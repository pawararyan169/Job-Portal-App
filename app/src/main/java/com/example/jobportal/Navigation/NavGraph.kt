package com.example.jobportal.navigation // FIX: Assuming lowercase 'navigation' package

import androidx.compose.material.Text // FIX: Import Text composable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jobportal.ui.AuthViewModel // FIX: Import AuthViewModel
import com.example.jobportal.ui.LoginScreen
import com.example.jobportal.ui.SignUpScreen
import com.example.jobportal.ui.RecruiterPostJobScreen
import com.example.jobportal.ui.JobSeekerJobListScreen // FIX: Assuming this screen exists

// FIX: Define Screen routes ONLY here to avoid Redeclaration error
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object PostJob : Screen("post_job/{recruiterId}") {
        fun createRoute(recruiterId: Int) = "post_job/$recruiterId"
    }
}

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    userEmail: String?
) {
    // FIX: AuthViewModel is resolved via import
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        // --- Login Screen ---
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
            )
        }

        // --- Sign Up Screen ---
        composable(Screen.SignUp.route) {
            // FIX: Removed 'onSignUpSuccess' parameter from call since SignUpScreen doesn't seem to accept it
            SignUpScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.Login.route) } // Assuming it navigates back to login
            )
        }

        // --- Home Screen ---
        composable(Screen.Home.route) {
            // FIX: Using Text from material/compose to resolve "Unresolved reference 'Text'"
            Text("Welcome Home! User: $userEmail")
            // In a real app, this should display JobSeekerJobListScreen or RecruiterHomeScreen
            JobSeekerJobListScreen()
        }

        // --- Post Job Screen ---
        composable(Screen.PostJob.route) { backStackEntry ->
            val recruiterId = backStackEntry.arguments?.getString("recruiterId")?.toIntOrNull() ?: 0
            RecruiterPostJobScreen(recruiterId = recruiterId)
        }
    }
}
