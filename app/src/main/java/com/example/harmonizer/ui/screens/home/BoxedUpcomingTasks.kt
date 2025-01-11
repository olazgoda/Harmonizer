package com.example.harmonizer.ui.screens.home

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.example.harmonizer.ui.viewmodels.HouseholdViewModel

@Composable
fun BoxScope.BoxedUpcomingTasks(viewModel: HouseholdViewModel) {
    val household by viewModel.household.observeAsState()
    val isErrorActive by viewModel.isErrorActive.observeAsState(initial = false)
    val errorMessage by viewModel.errorMessage.observeAsState(initial = "")
    if (isErrorActive) {
        Text(
            "Error: $errorMessage",
            color = Color.Red,
            modifier = Modifier.align(Alignment.Center)
        )
    } else {
        household?.let {
            Text(
                "Gospodarstwo: ${it.name}",
                modifier = Modifier.align(Alignment.TopCenter)
            )
            LazyColumn(modifier = Modifier.align(Alignment.Center)) {
                items(count = household?.tasks?.size ?: 0) { index ->
                    val task = household?.tasks?.get(index)
                    task?.let {
                        ListItem(
                            headlineContent = {Text(it.name)},
                            leadingContent = {Text(it.description)},
                            supportingContent = {Text(it.dueDate.toString())},
                            modifier = Modifier.padding(8.dp))
                    }
                }
            }
        } ?: CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}