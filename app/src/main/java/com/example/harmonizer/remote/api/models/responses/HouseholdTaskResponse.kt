package com.example.harmonizer.remote.api.models.responses

import com.example.harmonizer.remote.serializers.ZoneDateTimeKSerializer
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class HouseholdTaskResponse(
    val id: Int,
    val householdId: Int,
    val name: String,
    val description: String,
    @Serializable(with = ZoneDateTimeKSerializer::class)
    val dueDate: ZonedDateTime,  // Date as ISO 8601 string (e.g., "2024-01-01T00:00:00Z")
    val assignedMemberId: Int?,
    val assignedMember: HouseholdMemberResponse?,
    val isDone: Boolean
)
