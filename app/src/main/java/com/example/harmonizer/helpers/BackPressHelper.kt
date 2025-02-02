package com.example.harmonizer.helpers

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun DisableBackPress() {
    BackHandler(enabled = true) {

    }
}