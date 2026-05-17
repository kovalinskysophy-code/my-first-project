package com.example

import kotlinx.serialization.Serializable

@Serializable
data class ComputeRequest(
    val matrix: List<List<Double>>,
    val operation: String
)

@Serializable
data class ComputeResponse(
    val taskId: String,
    val status: String
)

@Serializable
data class ResultResponse(
    val taskId: String,
    val result: String?,  // ← ИЗМЕНИТЕ: было Any?, стало String?
    val error: String?
)