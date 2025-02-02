package com.example.harmonizer.ui.screens.home.households

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.harmonizer.remote.api.models.responses.UserHouseholdResponse
import com.example.harmonizer.ui.theme.outlinedTextFieldColors
import com.example.harmonizer.ui.viewmodels.HouseholdViewModel
import com.example.harmonizer.ui.viewmodels.UserViewModel

@Composable
fun HouseholdsDropdown(
    householdViewModel: HouseholdViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier? = Modifier,
    onItemSelected: (Int) -> Unit,
    updateShowNewHouseholdDialog: (Boolean) -> Unit
) {
    val userData = userViewModel.user.observeAsState()
    val selectedHouseholdId = householdViewModel.selectedHouseholdId.observeAsState()
    val selectedHouseholdName = householdViewModel.selectedHouseholdName.observeAsState()
    var expanded by remember { mutableStateOf(false) }
    val items = {userData.value?.households ?: emptyList() }

    Box(modifier = modifier ?: Modifier) {
        OutlinedTextField(
            value = selectedHouseholdName.value!!,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            colors = outlinedTextFieldColors(),
            label = { Text("Aktywne gospodarstwo") },
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown, contentDescription = "Dropdown Icon",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items().forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.householdName) },
                    onClick = {
                        onItemSelected(item.householdId)
                        expanded = false
                    })
            }

            DropdownMenuItem(
                text = { Text("+ Stwórz nowe gospodarstwo") },
                onClick = {
                    updateShowNewHouseholdDialog(true)
                })
        }
    }
}