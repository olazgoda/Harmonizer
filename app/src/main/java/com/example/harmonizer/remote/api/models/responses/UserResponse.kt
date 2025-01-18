package com.example.harmonizer.remote.api.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val firstName: String,
    val lastName: String,
    val id: Int,
    val email: String,
    val households: List<UserHouseholdResponse>
)