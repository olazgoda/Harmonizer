package com.example.harmonizer.ui.screens.home.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.harmonizer.remote.api.models.responses.HouseholdMemberResponse
import com.example.harmonizer.ui.theme.outlinedTextFieldColors

@Composable
fun MemberDropdown(
    selectedMemberId: Int?,
    items: List<HouseholdMemberResponse>,
    onItemSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxWidth()) {
        OutlinedTextField(
            value =
            if (selectedMemberId == null)
                "Wybierz osobę"
            else {
                val selectedMember = items.firstOrNull(predicate = {it.id == selectedMemberId })
                selectedMember?.firstName + " " + selectedMember?.lastName;
            },
            onValueChange = {},
            readOnly = true,
            enabled = false,
            colors = outlinedTextFieldColors(),
            label = {Text("Przypisano do")},
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
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.firstName + " " + item.lastName) },
                    onClick = {
                        onItemSelected(item.id)
                        expanded = false
                    })
            }
        }
    }
}