package com.example.jobportal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.example.jobportal.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.jobportal.model.UserDatabase
import com.example.jobportal.navigation.AppNavGraph
import com.example.jobportal.navigation.Screen
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.ui.text.font.FontWeight

class MainActivity : ComponentActivity() {

    private val credentialManager by lazy { CredentialManager.create(this) }
    private val db by lazy { UserDatabase.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                var user by remember { mutableStateOf<User?>(null) }
                var isLoading by remember { mutableStateOf(true) }

                // Load existing user from DB
                LaunchedEffect(true) {
                    withContext(Dispatchers.IO) {
                        user = db.userDao().getUser()
                        isLoading = false
                    }
                }

                if (isLoading) {
                    // Show a simple loading state while checking the DB
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    // FIX: Removed the redundant 'initialUserLoggedIn' parameter
                    AppNavGraph(
                        startDestination = if (user == null) Screen.Login.route else Screen.Home.route,
                        userEmail = user?.email
                    )
                }
            }
        }
    }

    // --- LoginScreen composable refactored for temporary use as landing page (Login/Sign-In UI) ---
    @Composable
    fun LoginScreen(onLoggedIn: (User) -> Unit) {
        val gradient = Brush.verticalGradient(
            colors = listOf(Color(0xFFE3F2FD), Color(0xFFFFFFFF))
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Job Portal", style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = { startGoogleSignIn(onLoggedIn) }) {
                    Text("Sign in with Google")
                }
                TextButton(onClick = { /* navigate to Manual Login */ }) {
                    Text("Or sign in manually")
                }
            }
        }
    }

    // --- Google Sign-In Logic ---
    private fun startGoogleSignIn(onLoggedIn: (User) -> Unit) {
        lifecycleScope.launch {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result: GetCredentialResponse = credentialManager.getCredential(
                    this@MainActivity,
                    request
                )

                val credential = result.credential
                if (credential is GoogleIdTokenCredential) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                    val user = User(
                        name = googleIdTokenCredential.displayName,
                        email = googleIdTokenCredential.id,
                        photoUrl = googleIdTokenCredential.profilePictureUri?.toString()
                    )

                    withContext(Dispatchers.IO) {
                        db.userDao().insertUser(user)
                    }

                    onLoggedIn(user)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error
            }
        }
    }
}
