package com.example.harmonizer.ui.screens.home.tasks
import android.content.Context
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.harmonizer.helpers.toZonedDateTime
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarmonizerDatePicker(
    initialDate: ZonedDateTime,
    updateDate: (newDate: ZonedDateTime)->Unit,
    updateIsDatePickerShown: (Boolean)->Unit
)
{
    val initialDateMillis = {initialDate.toEpochSecond() * 1000};
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis(),
        initialDisplayMode = DisplayMode.Picker,
        initialDisplayedMonthMillis = initialDateMillis()
    );

    DatePickerDialog(
        confirmButton = {
            TextButton(onClick = {
                updateDate(datePickerState.selectedDateMillis!!.toZonedDateTime())
                updateIsDatePickerShown(false)
            },
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                updateIsDatePickerShown(false)}){
                Text("Cancel")
            }
        },
        onDismissRequest = {updateIsDatePickerShown(false)},
    ) {
        DatePicker(
            state = datePickerState,

        )
    }
}
