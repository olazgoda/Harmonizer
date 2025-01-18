package com.example.harmonizer.remote.api.models.responses
import kotlinx.serialization.Serializable

@Serializable
data class UserHouseholdResponse(
    val householdId: Int,
    val memberId: Int,
    val householdName: String
)