package com.example.harmonizer.remote.api.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class HouseholdInvitationResponse(
    val id: Int,
    val householdId: Int,
    val email: String
)
