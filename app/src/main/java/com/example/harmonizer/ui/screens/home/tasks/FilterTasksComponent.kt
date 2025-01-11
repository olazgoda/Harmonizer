package com.example.harmonizer.ui.screens.home.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class TaskFilters(
    val showCompleted: Boolean,
    val showOverdue: Boolean,
    val showOngoing: Boolean
)

@Composable
fun FilterTasksComponent(
    filters: TaskFilters,
    onFilterChange: (TaskFilters) -> Unit
) {
    var tempFilters by remember { mutableStateOf(filters) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Filtry", style = MaterialTheme.typography.titleMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = tempFilters.showCompleted,
                onCheckedChange = {
                    tempFilters = tempFilters.copy(showCompleted = it)
                    onFilterChange(tempFilters)
                }
            )
            Text(text = "Zakończone")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = tempFilters.showOverdue,
                onCheckedChange = {
                    tempFilters = tempFilters.copy(showOverdue = it)
                    onFilterChange(tempFilters)
                }
            )
            Text(text = "Przedawnione")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = tempFilters.showOngoing,
                onCheckedChange = {
                    tempFilters = tempFilters.copy(showOngoing = it)
                    onFilterChange(tempFilters)
                }
            )
            Text(text = "Bieżące")
        }
    }
}