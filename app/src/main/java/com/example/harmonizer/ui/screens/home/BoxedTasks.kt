package com.example.harmonizer.ui.screens.home

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.harmonizer.ui.viewmodels.HouseholdViewModel
import java.time.ZonedDateTime
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import com.example.harmonizer.ui.screens.home.tasks.FilterTasksComponent
import com.example.harmonizer.ui.screens.home.tasks.NewTaskDialog
import com.example.harmonizer.ui.screens.home.tasks.TaskFilters
import com.example.harmonizer.ui.screens.home.tasks.TaskItem

@Composable
fun BoxScope.BoxedTasks(viewModel: HouseholdViewModel) {
    val dateTimeNow = ZonedDateTime.now()
    val household by viewModel.household.observeAsState()
    var showNewTaskDialog by remember { mutableStateOf(false) }
    var showFilteringDropdown by remember { mutableStateOf(false) }
    var taskFilters by remember { mutableStateOf(TaskFilters(true, true, true)) }

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
            Text(
                text = "Zadania",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
            )
            IconButton(onClick = {showFilteringDropdown = true}) {
                Icon(Icons.Default.MoreVert, contentDescription = "Filter")
            }
            IconButton(
                onClick = { showNewTaskDialog = true },
                modifier = Modifier
                    .shadow(elevation = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
            }
        }
        if (showFilteringDropdown) {
            DropdownMenu(
                expanded = showFilteringDropdown,
                onDismissRequest = { showFilteringDropdown = false }
            ) {
                FilterTasksComponent(filters = taskFilters) { newFilters ->
                    taskFilters = newFilters
                }
            }
        }
    }

    if(showNewTaskDialog) {
        NewTaskDialog(members = household!!.members, viewModel) { showNewTaskDialog = it }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 48.dp),
    ) {
        household?.tasks?.let { tasks ->
            val filteredTasks = tasks.filter { task ->
                (!taskFilters.showOngoing && !taskFilters.showOverdue && !taskFilters.showCompleted)
                        ||
                (taskFilters.showCompleted && task.isDone) ||
                        (taskFilters.showOverdue && task.dueDate < dateTimeNow && !task.isDone) ||
                        (taskFilters.showOngoing && (task.dueDate >= dateTimeNow) && !task.isDone)
            }.sortedBy { it.dueDate }

            items(items = filteredTasks)
            { task ->
                TaskItem(
                    task = task,
                    household!!.members,
                    dateTimeNow,
                    viewModel = viewModel
                )
            }
        }
    }

}


