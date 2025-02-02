package com.example.harmonizer.ui.screens.home.households

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.harmonizer.ui.viewmodels.HouseholdViewModel
import com.example.harmonizer.ui.viewmodels.UserViewModel

@Composable
fun NewHouseholdDialog(
    householdViewModel: HouseholdViewModel,
    userViewModel: UserViewModel,
    updateShowNewHouseholdDialog: (Boolean) -> Unit
) {
    var newHouseholdName by remember { mutableStateOf("") }
    val isHouseholdNameEmpty = newHouseholdName.isEmpty();
    val userData by userViewModel.user.observeAsState();
    val isHouseholdNameExisting =
        userData?.households?.any { it.householdName == newHouseholdName } ?: false
    val errorMessage = when {
        isHouseholdNameEmpty -> {
            "Wpisz wartość"
        }

        isHouseholdNameExisting -> {
            "Gospodarstwo o tej nazwie już istnieje"
        }

        else -> {
            ""
        }
    }
    val isError = isHouseholdNameEmpty || isHouseholdNameExisting

    Dialog(
        onDismissRequest = {
            updateShowNewHouseholdDialog(false)
        },
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = newHouseholdName,
                    onValueChange = { newHouseholdName = it },
                    label = { Text("Nazwa", style = MaterialTheme.typography.titleSmall) },
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
                            householdViewModel.createHousehold(newHouseholdName)
                            userViewModel.refreshUser()
                            updateShowNewHouseholdDialog(false)
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