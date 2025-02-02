package com.example.harmonizer.ui.screens.home.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.harmonizer.R
import com.example.harmonizer.helpers.toDateString
import com.example.harmonizer.remote.api.models.responses.HouseholdTaskResponse
import com.example.harmonizer.ui.viewmodels.HouseholdViewModel
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun TaskDetailsDialog(
    taskId: Int,
    viewModel: HouseholdViewModel,
    updateOpenDetailsTask: (openDetailsTask: HouseholdTaskResponse?) -> Unit
) {
    val householdState = viewModel.household.observeAsState()
    val task = householdState.value!!.tasks.first { it.id == taskId }
    val members = householdState.value!!.members;
    var selectedMember by remember { mutableStateOf(task.assignedMemberId) }
    var isTaskDone by remember { mutableStateOf(task.isDone) }
    var isEditingTaskTitle by remember { mutableStateOf(false) }
    var isEditingTaskDesc by remember { mutableStateOf(false) }
    var taskCurrentTitle by remember { mutableStateOf(task.name) }
    var taskCurrentDesc by remember { mutableStateOf(task.description) }

    var isDatePickerShown by remember { mutableStateOf(false) }
    if (isDatePickerShown) {
        HarmonizerDatePicker(
            task.dueDate,
            updateDate = { viewModel.updateTaskDueDate(task.id, it.toEpochSecond() * 1000) },
            updateIsDatePickerShown = { isDatePickerShown = it }
        )
    }

    Dialog(
        onDismissRequest = {
            updateOpenDetailsTask(null)
        }, properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Tytuł", style = MaterialTheme.typography.titleSmall)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (isEditingTaskTitle) {
                        TextField(
                            modifier = Modifier.weight(4f),
                            value = taskCurrentTitle,
                            onValueChange = { taskCurrentTitle = it },
                            textStyle = MaterialTheme.typography.titleSmall,
                            singleLine = false
                        )
                    } else {
                        Text(
                            modifier = Modifier.weight(4f),
                            text = taskCurrentTitle,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    IconButton(modifier = if (isEditingTaskTitle) Modifier.weight(1f) else Modifier,
                        onClick = {
                            isEditingTaskTitle = !isEditingTaskTitle

                            if (!isEditingTaskTitle) {
                                viewModel.updateTaskTitle(
                                    task.id, taskCurrentTitle
                                )
                            }
                        }) {
                        Icon(
                            imageVector = if (isEditingTaskTitle) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = if (isEditingTaskTitle) "Zatwierdź edycję" else "Edytuj pole"
                        )
                    }
                }

                Text(text = "Opis", style = MaterialTheme.typography.titleSmall)
                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (isEditingTaskDesc) {
                        TextField(
                            modifier = Modifier.weight(4f),
                            value = taskCurrentDesc,
                            onValueChange = { taskCurrentDesc = it },
                            textStyle = MaterialTheme.typography.titleSmall,
                            singleLine = false
                        )
                    } else {
                        Text(
                            modifier = Modifier.weight(4f),
                            text = taskCurrentDesc,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    IconButton(modifier = if (isEditingTaskDesc) Modifier.weight(1f) else Modifier,
                        onClick = {
                            isEditingTaskDesc = !isEditingTaskDesc

                            if (!isEditingTaskDesc) {
                                viewModel.updateTaskDesc(
                                    task.id, taskCurrentDesc
                                )
                            }
                        }) {
                        Icon(
                            imageVector = if (isEditingTaskDesc) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = if (isEditingTaskDesc) "Zatwierdź edycję" else "Edytuj pole"
                        )
                    }
                }
                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Termin: ", style = MaterialTheme.typography.titleSmall)
                    Text(
                        text = task.dueDate.toDateString(),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    IconButton(
                        onClick = {
                            isDatePickerShown = true
                        },
                    ) {
                        Icon(Icons.Default.Edit, "Edytuj datę")
                    }

                }
                Row {
                    MemberDropdown(selectedMember, items = members, onItemSelected = {
                        selectedMember = it
                        viewModel.assignTaskToMember(task.id, it)
                    })
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.deleteTask(task.id)
                            updateOpenDetailsTask(null)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Usuń",
                            tint = Color.White
                        )
                        Text(
                            text = "Usuń",
                            color = Color.White,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            isTaskDone = !isTaskDone
                            viewModel.updateTaskDoneStatus(task.id, isTaskDone)
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isTaskDone) Color(0xFFA5D6A7) else Color(
                                0xFFEF9A9A
                            )
                        )
                    ) {
                        Text(
                            text = if (isTaskDone) "Przywróć zadanie" else "Ukończ zadanie",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


