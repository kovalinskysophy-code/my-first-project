package com.example

import kotlinx.coroutines.*

class ComputationEngine(private val storage: InMemoryStorage) {
    private val scope = CoroutineScope(Dispatchers.Default)

    fun startComputation(taskId: String, request: ComputeRequest) {


        scope.launch {
            try {
                val result = withContext(Dispatchers.Default) {
                    delay(2000)
                    when (request.operation.lowercase()) {
                        "determinant" -> calculateDeterminant(request.matrix).toString()
                        else -> throw IllegalArgumentException("Unknown operation")
                    }
                }
                storage.saveResult(taskId, result)
            } catch (e: Exception) {
                storage.saveError(taskId, e.message ?: "Unknown error")
            }
        }
    }

    private fun calculateDeterminant(matrix: List<List<Double>>): Double {
        val n = matrix.size
        if (n == 1) return matrix[0][0]
        if (n == 2) return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]

        var det = 0.0
        for (col in 0 until n) {
            val subMatrix = getSubMatrix(matrix, 0, col)
            val sign = if (col % 2 == 0) 1.0 else -1.0
            det += sign * matrix[0][col] * calculateDeterminant(subMatrix)
        }
        return det
    }

    private fun getSubMatrix(matrix: List<List<Double>>, excludeRow: Int, excludeCol: Int): List<List<Double>> {
        val result = mutableListOf<List<Double>>()
        for (i in matrix.indices) {
            if (i == excludeRow) continue
            val row = mutableListOf<Double>()
            for (j in matrix.indices) {
                if (j == excludeCol) continue
                row.add(matrix[i][j])
            }
            result.add(row)
        }
        return result
    }
}

