package com.example.harmonizer.ui.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8B8)

val Purple40 = Color(0xFFF44336)
val PurpleGrey40 = Color(0xFFF44336)
val Pink40 = Color(0xFF7D5260)

@Composable
fun outlinedTextFieldColors() =
OutlinedTextFieldDefaults.colors(
    disabledTextColor = MaterialTheme.colorScheme.onSurface,
    disabledContainerColor = Color.Transparent,
    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedBorderColor = Color(0x00FF0000),
    unfocusedBorderColor = Color(0x00FF0000),
    disabledBorderColor = Color(0x00FF0000)
)