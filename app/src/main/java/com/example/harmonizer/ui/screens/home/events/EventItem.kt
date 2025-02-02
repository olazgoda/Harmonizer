package com.example.harmonizer.ui.screens.home.events

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.harmonizer.remote.api.models.responses.HouseholdEventResponse
import com.example.harmonizer.remote.api.models.responses.HouseholdTaskResponse
import com.example.harmonizer.ui.viewmodels.HouseholdViewModel

@Composable
fun EventItem(
    event: HouseholdEventResponse,
    householdViewModel: HouseholdViewModel,
    updateOpenDetailsTaskId: (openDetailsTaskId: HouseholdTaskResponse?) -> Unit
) {
    val household by householdViewModel.household.observeAsState();
    val relatedTask = household?.tasks?.firstOrNull { it.id == event.taskId }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF36C1CE)
        ),
        onClick = {updateOpenDetailsTaskId(relatedTask)}
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (event.eventType == 1) {
                Text(
                    text = "Zadanie '${relatedTask?.name}' zostało do ciebie przypisane",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.DarkGray
                )
            }

            Text(
                text = "Kliknij, aby podejrzeć zadanie",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
    }
}