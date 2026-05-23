package com.example

import java.util.concurrent.ConcurrentHashMap

class InMemoryStorage {
    private val results = ConcurrentHashMap<String, String>()  // ← было Any?, стало String
    private val errors = ConcurrentHashMap<String, String>()

    fun saveResult(id: String, result: String) { results[id] = result }  // ← String
    fun saveError(id: String, error: String) { errors[id] = error }
    fun getResult(id: String): String? = results[id]  // ← String?
    fun getError(id: String): String? = errors[id]
}
