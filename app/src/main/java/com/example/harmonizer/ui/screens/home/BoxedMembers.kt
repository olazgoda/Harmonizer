package com.example.harmonizer.ui.screens.home

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.harmonizer.ui.screens.home.members.MemberItem
import com.example.harmonizer.ui.screens.home.members.NewMemberDialog
import com.example.harmonizer.ui.viewmodels.HouseholdViewModel

@Composable
fun BoxScope.BoxedMembers(viewModel: HouseholdViewModel) {
    val household by viewModel.household.observeAsState()
    var showNewMemberDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopStart)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Domownicy",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
            )
            IconButton(
                onClick = { showNewMemberDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
            }
        }
    }

    if (showNewMemberDialog) {
        NewMemberDialog(members = household!!.members, viewModel) { showNewMemberDialog = it }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 48.dp),
    ) {
        if (household?.members != null) {
            items(items = household!!.members)
            { member ->
                MemberItem(
                    member = member,
                    onDeleteMember = { viewModel.deleteMember(member.id) }
                )
            }
        }
    }
}