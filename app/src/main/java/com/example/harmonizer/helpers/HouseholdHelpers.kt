package com.example.harmonizer.helpers

import android.content.Context

fun saveHouseholdData(context: Context, householdId: Int) {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("selected_household_id", householdId).apply()
}

fun getHouseholdData(context: Context): Int? {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    val householdId = prefs.getInt("selected_household_id", 0)
    if (householdId == 0) {
        return null
    }
    return householdId
}