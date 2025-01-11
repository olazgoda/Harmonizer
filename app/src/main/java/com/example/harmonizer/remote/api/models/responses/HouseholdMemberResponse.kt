package com.example.harmonizer.remote.api.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class HouseholdMemberResponse(
    val id: Int,
    val householdId: Int,
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val email: String
)

