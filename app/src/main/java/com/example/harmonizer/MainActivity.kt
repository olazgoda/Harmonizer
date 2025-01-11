package com.example.harmonizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.harmonizer.helpers.getStoredJwt
import com.example.harmonizer.helpers.isTokenValid
import com.example.harmonizer.ui.theme.HarmonizerTheme
import com.example.harmonizer.ui.dictionary.ScreenName
import com.example.harmonizer.ui.screens.AuthScreen
import com.example.harmonizer.ui.screens.home.HomeScreen
import com.example.harmonizer.ui.screens.LoginScreen
import com.example.harmonizer.ui.screens.RegisterScreen
import com.example.harmonizer.ui.viewmodels.HouseholdViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HarmonizerTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                LaunchedEffect(Unit) {
                    val jwt = getStoredJwt(context)
                    if (jwt != null && isTokenValid(jwt)) {
                        navController.navigate(ScreenName.Home)
                    } else {
                        navController.navigate(ScreenName.Auth)
                    }
                }
                AppNavHost(navController = navController)
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = ScreenName.Auth) {
        composable(ScreenName.Home) {
            HomeScreen(navController, HouseholdViewModel(context = LocalContext.current))
        }
        composable(ScreenName.Auth) {
            AuthScreen(navController)
        }
        composable(ScreenName.Login) {
            LoginScreen(navController)
        }
        composable(ScreenName.Register) {
            RegisterScreen(navController);
        }

    }
}