package com.example.harmonizer.ui.screens.home.tasks

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Resources.Theme
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import com.example.harmonizer.R
import java.time.ZoneId
import java.time.ZonedDateTime


fun harmonizerDatePicker(
    context: Context,
    initialDate: ZonedDateTime,
    updateDate: (newDate: ZonedDateTime)->Unit
):DatePickerDialog
{
    return DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = ZonedDateTime.of(
                year, month+1, dayOfMonth, 23, 59, 59, 59, ZoneId.systemDefault()
            )
            updateDate(selectedDate)
        }, initialDate.year, initialDate.monthValue-1, initialDate.dayOfMonth
    )
}
