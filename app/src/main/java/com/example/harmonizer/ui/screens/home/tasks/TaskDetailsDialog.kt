package com.example.harmonizer.ui.screens.home.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import com.example.harmonizer.helpers.toDateString
import com.example.harmonizer.remote.api.models.responses.HouseholdMemberResponse
import com.example.harmonizer.remote.api.models.responses.HouseholdTaskResponse
import com.example.harmonizer.ui.theme.RedButtonColors
import com.example.harmonizer.ui.viewmodels.HouseholdViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsDialog(
    task: HouseholdTaskResponse,
    members: List<HouseholdMemberResponse>,
    viewModel: HouseholdViewModel,
    updateShowTaskDetailsState: (isTaskDetailsShown: Boolean) -> Unit
) {
    var selectedMember by remember { mutableStateOf(task.assignedMemberId) }
    var isTaskDone by remember { mutableStateOf(task.isDone) }
    var isDatePickerOpen by remember { mutableStateOf(false) }
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = task.dueDate.toEpochSecond() * 1000
        )

    Dialog(
        onDismissRequest = {
            updateShowTaskDetailsState(false)
        },
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Opis", style = MaterialTheme.typography.titleSmall)
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(modifier = Modifier.height(IntrinsicSize.Max), verticalAlignment = Alignment.CenterVertically) {
                    Text("Termin: ", style = MaterialTheme.typography.titleSmall)
                    Text(
                        text = datePickerState.selectedDateMillis!!.toDateString(),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    IconButton(onClick = {
                        isDatePickerOpen = true;
                    }) {
                        Icon(Icons.Default.Edit, "Edit date")
                    }
                }
                if (isDatePickerOpen) {
                    Dialog(
                        onDismissRequest = {
                            isDatePickerOpen = false;
                            viewModel.updateTaskDueDate(
                                task.id,
                                datePickerState.selectedDateMillis!!
                            )
                        },
                        properties = DialogProperties(dismissOnClickOutside = true)
                    ) {
                        Card(
                            modifier = Modifier.padding(4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ){
                            DatePicker(
                                state = datePickerState,
                                showModeToggle = false
                            )
                        }
                    }
                }
                MemberDropdown(selectedMember, items = members, onItemSelected = {
                    selectedMember = it
                    viewModel.assignTaskToMember(task.id, it)
                }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Button(
                        onClick = {
                            isTaskDone = !isTaskDone
                            viewModel.updateTaskDoneStatus(task.id, isTaskDone)
                        },
                        colors = RedButtonColors
                    )
                    {
                        Text(text = if (isTaskDone) "Przywróć zadanie" else "Zakończ zadanie")
                    }
                }
            }
        }
    }
}