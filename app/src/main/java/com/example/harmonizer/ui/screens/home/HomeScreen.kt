package com.example.harmonizer.ui.screens.home

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.harmonizer.ui.viewmodels.HouseholdViewModel
import com.example.harmonizer.ui.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HouseholdViewModel) {
    val items = listOf("Home", "Calendar", "Notifications", "Profile")
    var selectedItem by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    LaunchedEffect(viewModel.selectedHouseholdId.value) {
        viewModel.refreshHouseholdData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Harmonizer", style = MaterialTheme.typography.headlineSmall)
                },
                actions = {
                    IconButton(
                        onClick = {
                            selectedItem = 4;
                            viewModel.refreshHouseholdData()
                        }) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            when (index) {
                                0 -> Icon(Icons.Default.Home, contentDescription = item)
                                1 -> Icon(Icons.Default.DateRange, contentDescription = item)
                                2 -> {
                                    BadgedBox(badge = {
                                        Badge(containerColor = Color.Red) { Text("0") }
                                    }) {
                                        Icon(Icons.Default.Notifications, contentDescription = item)
                                    }
                                }

                                3 -> Icon(
                                    Icons.Default.Person,
                                    contentDescription = item
                                )
                            }
                        },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            viewModel.refreshHouseholdData()
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedItem) {
                0 -> BoxedUpcomingTasks(viewModel)
                1 -> BoxedTasks(viewModel)
//                2 -> BoxedEvents(viewModel)
                3 -> BoxedMembers(viewModel)
                4 -> BoxedProfile(UserViewModel(context), navController)

            }
        }
    }
}




