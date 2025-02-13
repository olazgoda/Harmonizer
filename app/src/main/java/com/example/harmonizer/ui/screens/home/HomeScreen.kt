package com.example.harmonizer.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.harmonizer.helpers.DisableBackPress
import com.example.harmonizer.ui.viewmodels.HouseholdViewModel
import com.example.harmonizer.ui.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    householdViewModel: HouseholdViewModel,
    userViewModel: UserViewModel,
    updateIsUserAuthorized: (Boolean) -> Unit
) {
    DisableBackPress()
    val items = listOf("Home", "Calendar", "Notifications", "Profile")
    var selectedItem by remember { mutableIntStateOf(0) }
    val householdEvents by householdViewModel.householdEvents.observeAsState()
    val isHouseholdErrorActive by householdViewModel.isErrorActive.observeAsState()
    val isUserErrorActive by userViewModel.isErrorActive.observeAsState()
    val currentUser by userViewModel.user.observeAsState()
    val household by householdViewModel.household.observeAsState()
    val currentMemberId = { household?.members?.firstOrNull { it.userId == currentUser?.id }?.id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Harmonizer", style = MaterialTheme.typography.headlineSmall)
                },
                actions = {
                    IconButton(
                        onClick = {
                            userViewModel.refreshUser()
                            if (userViewModel.user.value != null) {
                                selectedItem = 4
                                householdViewModel.refreshHouseholdData()
                            }
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
                                        Badge(containerColor = Color.Red)
                                        { Text("${householdEvents?.count { it.memberId == currentMemberId() } ?: 0}") }
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
                            if (selectedItem == 2 && index != 2) {
                                householdViewModel.markEventsAsRead()
                            }
                            selectedItem = index
                            householdViewModel.refreshHouseholdData()
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
                0 -> BoxedUpcomingTasks(householdViewModel, userViewModel)
                1 -> BoxedTasks(householdViewModel)
                2 -> BoxedEvents(householdViewModel, userViewModel)
                3 -> BoxedMembers(householdViewModel)
                4 -> BoxedProfile(userViewModel, navController, updateIsUserAuthorized)
            }
        }
    }

    if (isHouseholdErrorActive == true || isUserErrorActive == true) {
        Column {
            Row(modifier = Modifier.weight(6f)) {}
            if (isHouseholdErrorActive == true) {
                Row {
                    Snackbar(
                        action = {
                            IconButton(onClick = { householdViewModel.clearErrorState() }) {
                                Icon(Icons.Default.Clear, "Zamknij")
                            }
                        }
                    ) {
                        Text(householdViewModel.errorMessage.value ?: "")
                    }
                }
            }
            if (isUserErrorActive == true) {
                Row {
                    Snackbar(
                        action = {
                            IconButton(onClick = { userViewModel.clearErrorState() }) {
                                Icon(Icons.Default.Clear, "Zamknij")
                            }
                        }
                    ) {
                        Text(userViewModel.errorMessage.value ?: "")
                    }
                }
            }
            Row(modifier = Modifier.weight(1f)) {}
        }
    }
}




