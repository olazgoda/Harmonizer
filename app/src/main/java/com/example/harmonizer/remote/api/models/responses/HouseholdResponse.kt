package com.example.harmonizer.remote.api.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class HouseholdResponse(
    val id: Int,
    val name: String,
    val invitations: List<HouseholdInvitationResponse>,
    val members: List<HouseholdMemberResponse>,
    val tasks: List<HouseholdTaskResponse>
)

