package com.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun main() {
    embeddedServer(Netty, port = 8081) {
        install(ContentNegotiation) {
            json()
        }

        val storage = InMemoryStorage()
        val engine = ComputationEngine(storage)

        routing {
            post("/compute") {
                val request = call.receive<ComputeRequest>()
                val taskId = UUID.randomUUID().toString()
                engine.startComputation(taskId, request)
                call.respond(ComputeResponse(taskId, "processing"))
            }

            get("/result/{taskId}") {
                val taskId = call.parameters["taskId"] ?: return@get call.respond(ResultResponse("", null, "Missing taskId"))
                val result = storage.getResult(taskId)
                val error = storage.getError(taskId)

                when {
                    error != null -> call.respond(ResultResponse(taskId, null, error))
                    result != null -> call.respond(ResultResponse(taskId, result, null))
                    else -> call.respond(ResultResponse(taskId, null, "Task not found or still processing"))
                }
            }

            get("/") {
                call.respondText("Compute Service is running! Use POST /compute and GET /result/{id}")
            }
        }
    }.start(wait = true)
}