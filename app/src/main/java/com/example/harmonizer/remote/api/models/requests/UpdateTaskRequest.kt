package com.example.harmonizer.remote.api.models.requests

import com.example.harmonizer.remote.serializers.ZoneDateTimeKSerializer
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class UpdateTaskRequest(
    val name: String? = null,
    val description: String? = null,
    @Serializable(with = ZoneDateTimeKSerializer::class)
    val dueDate: ZonedDateTime? = null,
    val isDone: Boolean? = null,
    val assignedMemberId : Int? = null
)
