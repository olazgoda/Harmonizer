package com.example.harmonizer.remote.api.models.responses

import com.example.harmonizer.remote.serializers.ZoneDateTimeKSerializer
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class HouseholdEventResponse(
    val id: Int,
    val householdId: Int,
    val memberId: Int,
    val eventType: Int,
    val taskId: Int,
    @Serializable(with = ZoneDateTimeKSerializer::class)
    val createdAt: ZonedDateTime,
    val isRead: Boolean
)
