package com.example.harmonizer.ui.screens.home.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.harmonizer.helpers.toDateString
import com.example.harmonizer.remote.api.models.responses.HouseholdMemberResponse
import com.example.harmonizer.remote.api.models.responses.HouseholdTaskResponse
import java.time.ZonedDateTime

@Composable
fun TaskItem(
    task: HouseholdTaskResponse,
    assignedMemberData: HouseholdMemberResponse?,
    dateTimeNow: ZonedDateTime,
    updateOpenDetailsTaskId: (openDetailsTaskId: HouseholdTaskResponse?) -> Unit
) {
    val isTaskOverdue = task.dueDate <= dateTimeNow.withHour(0).withMinute(0).withSecond(0)
    val assignedMemberName = assignedMemberData?.let { it.firstName + " " + it.lastName }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                task.isDone -> Color(0xFFA5D6A7)
                isTaskOverdue -> Color(0xFFEF9A9A)
                else -> Color(0xffffee58)
            }
        ),
        onClick = {
            updateOpenDetailsTaskId(task)
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = task.name, style = MaterialTheme.typography.bodyLarge, color = Color.DarkGray)
            Text(text = "Termin: ${task.dueDate.toDateString()}", style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
            Text(text = "Przypisano do: ${assignedMemberName ?: "Nie wskazano domownika"}", style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
        }
    }
}