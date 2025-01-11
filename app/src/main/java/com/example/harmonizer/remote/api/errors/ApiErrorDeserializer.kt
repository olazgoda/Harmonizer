package com.example.harmonizer.remote.api.errors

import kotlinx.serialization.json.Json

fun parseError(responseBody: String): String {
    val json = Json { ignoreUnknownKeys = true }  // Allows safe parsing even if the response has extra fields

    return try {
        val errorResponse = json.decodeFromString<ApiErrorResponse>(responseBody)
        errorResponse.errors?.entries?.joinToString("\n") {
            "${it.key}: ${it.value.joinToString(", ")}"
        } ?: errorResponse.detail ?: "Unknown error"
    } catch (e: Exception) {
        "Failed to parse error response"
    }
}