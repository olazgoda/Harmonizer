package com.example.harmonizer.remote.api.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class MarkAsReadRequest (
    val eventIds: List<Int>
)