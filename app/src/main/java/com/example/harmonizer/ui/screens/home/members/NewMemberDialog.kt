package com.example.harmonizer.ui.screens.home.members

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.harmonizer.remote.api.models.responses.HouseholdMemberResponse
import com.example.harmonizer.ui.viewmodels.HouseholdViewModel

@Composable
fun NewMemberDialog(
    members: List<HouseholdMemberResponse>,
    viewModel: HouseholdViewModel,
    updateShowNewMemberDialog: (showNewTaskDialog: Boolean) -> Unit
) {
    var newMemberEmail by remember { mutableStateOf("") }
    val isEmailEmpty = newMemberEmail.isEmpty();
    val isEmailAlreadyExisting = members.any{it.email == newMemberEmail}
    val errorMessage = when {
        isEmailEmpty -> {"Wpisz wartość"}
        isEmailAlreadyExisting -> {"Ten domownik już istnieje"}
        else -> {""}
    }
    val isError = members.any{it.email == newMemberEmail} || newMemberEmail.isEmpty()

    Dialog(
        onDismissRequest = {
            updateShowNewMemberDialog(false)
        },
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = newMemberEmail,
                    onValueChange = { newMemberEmail = it },
                    label = { Text("Email", style = MaterialTheme.typography.titleSmall) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                    isError = isError,
                    supportingText = {
                        if (isError) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.inviteNewMember(newMemberEmail)
                            updateShowNewMemberDialog(false);
                        }
                    )
                    {
                        Text(text = "Zaproś");
                    }
                }
            }
        }
    }
}