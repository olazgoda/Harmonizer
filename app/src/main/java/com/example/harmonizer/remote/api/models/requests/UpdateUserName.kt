package com.example.harmonizer.remote.api.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserName(
    val firstName: String,
    val lastName: String
)