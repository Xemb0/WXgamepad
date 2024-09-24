package com.autobot.connection

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.autobot.connection.theme.MyAppThemeComposable
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.koin.compose.currentKoinScope

@Composable
fun App() {
    // Fetch auth service instance
    val authService = AuthServiceImpl(auth = Firebase.auth)

    // Koin ViewModel - You can use Koin's built-in koinViewModel function
    val loginViewModel = LoginViewModel(authService)

    // MaterialTheme Wrapper
    MyAppThemeComposable {
        // Create a navigation controller
        val navController = rememberNavController()

        // Define the navigation host
        NavHost(
            navController = navController,
            startDestination = "LoginScreen"
        ) {
            // Define composable for the LoginScreen
            composable("LoginScreen") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("HomeScreen")
                    },
                    viewModel = loginViewModel
                )
            }

            // Add HomeScreen or other destinations as needed
            composable("HomeScreen") {
                // Your HomeScreen composable goes here
            }
        }
    }
}
@Composable
inline fun <reified T: ViewModel> koinViewModel(): T {
    val scope = currentKoinScope()
    return viewModel {
        scope.get<T>()
    }
}