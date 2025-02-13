package com.example.harmonizer.ui.screens.home

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.harmonizer.remote.api.models.responses.HouseholdTaskResponse
import com.example.harmonizer.ui.screens.home.households.HouseholdsDropdown
import com.example.harmonizer.ui.screens.home.households.NewHouseholdDialog
import com.example.harmonizer.ui.screens.home.tasks.TaskDetailsDialog
import com.example.harmonizer.ui.screens.home.tasks.TaskItem
import com.example.harmonizer.ui.viewmodels.HouseholdViewModel
import com.example.harmonizer.ui.viewmodels.UserViewModel
import java.time.ZonedDateTime

@Composable
fun BoxScope.BoxedUpcomingTasks(
    householdViewModel: HouseholdViewModel,
    userViewModel: UserViewModel
) {
    val household by householdViewModel.household.observeAsState()
    val currentUser by userViewModel.user.observeAsState()
    val selectedHouseholdId by householdViewModel.selectedHouseholdId.observeAsState()
    val currentMemberId = { household?.members?.firstOrNull { it.userId == currentUser?.id }?.id }
    var openTaskDetails by remember { mutableStateOf<HouseholdTaskResponse?>(null) }
    var showNewHouseholdDialog by remember { mutableStateOf(false) }
    val currentTime = ZonedDateTime.now()

    LaunchedEffect(key1 = currentUser) {
        userViewModel.refreshUser()
    }
    LaunchedEffect(key1 = selectedHouseholdId) {
        householdViewModel.refreshHouseholdData()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopStart)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HouseholdsDropdown(
                householdViewModel,
                userViewModel,
                onItemSelected = {
                    if (selectedHouseholdId != it) {
                        householdViewModel.updateSelectedHousehold(it)
                    }
                },
                updateShowNewHouseholdDialog = { showNewHouseholdDialog = it }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        {
            Text(text = "Nadchodzące zadania...")
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        )
        {
            household?.let {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    household?.tasks?.let { tasks ->
                        val filteredTasks = tasks
                            .filter { task ->
                                task.assignedMemberId == currentMemberId() &&
                                        !task.isDone && task.dueDate >= currentTime.withHour(0).withMinute(0).withSecond(0)
                            }
                            .sortedBy { it.dueDate }
                            .take(3)

                        items(items = filteredTasks, key = { it.id })
                        { task ->
                            TaskItem(
                                task = task,
                                household?.members?.firstOrNull { it.id == task.assignedMemberId },
                                currentTime,
                                { openTaskDetails = it }
                            )
                        }
                    }
                }
            }
        }

        openTaskDetails?.let { task ->
            TaskDetailsDialog(
                task.id,
                householdViewModel,
                updateOpenDetailsTask = { openTaskDetails = it })
        }

        if (showNewHouseholdDialog) {
            NewHouseholdDialog(
                householdViewModel,
                userViewModel,
                updateShowNewHouseholdDialog = { showNewHouseholdDialog = it }
            )
        }
    }
}