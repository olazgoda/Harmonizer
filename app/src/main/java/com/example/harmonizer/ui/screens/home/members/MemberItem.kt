package com.example.harmonizer.ui.screens.home.members

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.harmonizer.remote.api.models.responses.HouseholdMemberResponse

@Composable
fun MemberItem(
    member: HouseholdMemberResponse,
    onDeleteMember: (HouseholdMemberResponse) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showDeleteDialog = true
                    }
                )
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(Color(0xFF90CAF9)),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${member.firstName} ${member.lastName}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray
            )
            Text(
                text = member.email,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray
            )
        }
    }

    if (showDeleteDialog) {
        DeleteMemberDialog(
            onDismiss = { showDeleteDialog = false },
            onDelete = {
                onDeleteMember(member)
                showDeleteDialog = false
            }
        )
    }
}