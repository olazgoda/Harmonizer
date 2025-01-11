package com.example.harmonizer.remote.api.models.requests

import com.example.harmonizer.remote.serializers.ZoneDateTimeKSerializer
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class CreateTaskRequest(
    val name: String,
    val description: String,
    @Serializable(with = ZoneDateTimeKSerializer::class)
    val dueDate: ZonedDateTime,
    val assignedMemberId : Int?
)
