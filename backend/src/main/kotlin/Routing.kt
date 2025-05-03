package com.prudhvir3ddy.wtm

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable


@Serializable
data class SubmissionRequest(val url: String)

@Serializable
data class SubmissionResponse(val jobId: String, val message: String = "URL received and processing scheduled.")

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/generate") {
            val request = call.receive<SubmissionRequest>()
            val url = request.url

            val jobId = java.util.UUID.randomUUID().toString() // Placeholder for job ID
            println("Received URL: $url, Job ID: $jobId")

            call.respond(SubmissionResponse(jobId = jobId))
        }
    }
}
