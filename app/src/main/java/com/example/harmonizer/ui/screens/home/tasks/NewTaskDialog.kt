package com.example.harmonizer.ui.screens.home.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
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
import com.example.harmonizer.ui.viewmodels.HouseholdViewModel
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskDialog(
    members: List<HouseholdMemberResponse>,
    householdViewModel: HouseholdViewModel,
    updateShowNewTaskDialog: (isNewTaskDialogShown: Boolean) -> Unit
) {
    var selectedMemberId: Int? by remember { mutableStateOf(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf(ZonedDateTime.now()) }
    var isDatePickerShown by remember { mutableStateOf(false) }

    if (isDatePickerShown) {
        HarmonizerDatePicker(
            dueDate,
            updateDate = { dueDate = it },
            updateIsDatePickerShown = { isDatePickerShown = it }
        )
    }

    Dialog(
        onDismissRequest = {
            updateShowNewTaskDialog(false)
        },
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Tytuł", style = MaterialTheme.typography.titleSmall) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                    isError = title.isEmpty(),
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Opis", style = MaterialTheme.typography.titleSmall) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                    isError = description.isEmpty(),
                )
                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Termin: ",
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        text = dueDate!!.toDateString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    IconButton(onClick = {
                        isDatePickerShown = true
                    }) {
                        Icon(Icons.Default.Edit, "Edit date")
                    }
                }

                MemberDropdown(selectedMemberId, items = members, onItemSelected = {
                    selectedMemberId = it
                }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Button(
                        onClick = {
                            householdViewModel.createTask(title, description, dueDate!!, selectedMemberId)
                            updateShowNewTaskDialog(false);
                        }
                    )
                    {
                        Text(text = "Zapisz");
                    }
                }
            }
        }
    }
}