package com.example.harmonizer.remote.api.errors

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    @SerialName("type") val type: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("status") val status: Int? = null,
    @SerialName("detail") val detail: String? = null,
    @SerialName("traceId") val traceId: String? = null,
    @SerialName("errors") val errors: Map<String, List<String>>? = null
)
