package com.example.harmonizer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.harmonizer.helpers.DisableBackPress
import com.example.harmonizer.helpers.getFirstAppLaunch
import com.example.harmonizer.helpers.getStoredJwt
import com.example.harmonizer.helpers.isTokenValid
import com.example.harmonizer.helpers.updateFirstAppLaunch
import com.example.harmonizer.ui.theme.HarmonizerTheme
import com.example.harmonizer.ui.dictionary.ScreenName
import com.example.harmonizer.ui.screens.AuthScreen
import com.example.harmonizer.ui.screens.home.HomeScreen
import com.example.harmonizer.ui.screens.LoginScreen
import com.example.harmonizer.ui.screens.OnboardingScreen
import com.example.harmonizer.ui.screens.RegisterScreen
import com.example.harmonizer.ui.viewmodels.HouseholdViewModel
import com.example.harmonizer.ui.viewmodels.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HarmonizerTheme {
                DisableBackPress()
                val context = LocalContext.current
                var isUserAuthorized by remember { mutableStateOf(false) }
                var isFirstAppLaunch by remember { mutableStateOf(getFirstAppLaunch(context)) }
                val navController = rememberNavController()
                val userViewModel = UserViewModel(context)
                val householdViewModel = HouseholdViewModel(context)
                AppNavHost(navController = navController, userViewModel, householdViewModel,
                    updateIsUserAuthorized = { isUserAuthorized = it },
                    updateIsFirstAppLaunch = {
                        updateFirstAppLaunch(context, it); isFirstAppLaunch = it;
                    })
                val jwt = getStoredJwt(context)
                isUserAuthorized = jwt != null && isTokenValid(jwt)
                Log.d("IsUserAuthorized", "User authorization status: $isUserAuthorized")

                LaunchedEffect(key1 = isUserAuthorized, key2 = isFirstAppLaunch) {
                    if (isUserAuthorized && isFirstAppLaunch) {
                        Log.d("navigation", "Navigating to Onboarding")
                        navController.navigate(ScreenName.Onboarding)
                    } else if (isUserAuthorized) {
                        Log.d("navigation", "Navigating to Home")
                        navController.navigate(ScreenName.Home)

                    } else {
                        Log.d("navigation", "Navigating to auth")
                        navController.navigate(ScreenName.Auth)
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    userViewModel: UserViewModel,
    householdViewModel: HouseholdViewModel,
    updateIsUserAuthorized: (Boolean) -> Unit,
    updateIsFirstAppLaunch: (Boolean) -> Unit
) {
    NavHost(navController = navController, startDestination = ScreenName.Auth) {
        composable(ScreenName.Home) {
            HomeScreen(
                navController,
                householdViewModel,
                userViewModel,
                updateIsUserAuthorized
            )
        }
        composable(ScreenName.Auth) {
            AuthScreen(navController)
        }
        composable(ScreenName.Onboarding) {
            OnboardingScreen(updateIsFirstAppLaunch)
        }
        composable(ScreenName.Login) {
            LoginScreen(userViewModel, updateIsUserAuthorized)
        }
        composable(ScreenName.Register) {
            RegisterScreen(navController, updateIsUserAuthorized)
        }

    }
}