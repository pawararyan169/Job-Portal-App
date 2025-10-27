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
import androidx.credentials.CredentialManager
import com.example.jobportal.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.jobportal.model.UserDatabase
import com.example.jobportal.navigation.AppNavGraph
import com.example.jobportal.navigation.Screen
import androidx.compose.foundation.layout.fillMaxSize
import android.util.Log
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*

class MainActivity : ComponentActivity() {

    private val credentialManager by lazy { CredentialManager.create(this) }
    private val db by lazy { UserDatabase.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                var user by remember { mutableStateOf<User?>(null) }
                var isLoading by remember { mutableStateOf(true) }
                var userRole by remember { mutableStateOf<String?>(null) }
                var currentUserId by remember { mutableStateOf<String?>(null) }
                var isProfileComplete by remember { mutableStateOf(true) }

                LaunchedEffect(true) {
                    try {
                        withContext(Dispatchers.IO) {
                            user = db.userDao().getUser()
                            user?.let {
                                currentUserId = it.email
                                userRole = "jobseeker"
                                isProfileComplete = true
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("Startup", "FATAL DB INIT CRASH: ${e.message}", e)
                    } finally {
                        isLoading = false
                    }
                }

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    AppNavGraph(
                        startDestination = if (user == null) Screen.Login.route else Screen.Home.route,
                        userEmail = user?.email,
                        currentUserId = currentUserId,
                        userRole = userRole,
                        isProfileComplete = isProfileComplete
                    )
                }
            }
        }
    }

    // Google Sign-In Logic (Required for compilation)
    private fun startGoogleSignIn(onLoggedIn: (User) -> Unit) {
        lifecycleScope.launch {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId("YOUR_WEB_CLIENT_ID")
                    .setFilterByAuthorizedAccounts(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result: GetCredentialResponse = credentialManager.getCredential(this@MainActivity, request)

                val credential = result.credential
                if (credential is GoogleIdTokenCredential) {
                    val user = User(
                        name = "Google User",
                        email = "google@user.com",
                        photoUrl = null
                    )
                    withContext(Dispatchers.IO) { db.userDao().insertUser(user) }
                    onLoggedIn(user)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}