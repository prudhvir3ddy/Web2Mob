package com.prudhvir3ddy.wtm

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class SubmissionRequest(val url: String)

@Serializable
data class SubmissionResponse(val jobId: String, val message: String = "URL received and processing scheduled.")

@Serializable
data class ClientPayload(val url: String, val package_name: String)

@Serializable
data class GitHubRequest(val event_type: String, val client_payload: ClientPayload)

fun Application.configureRouting() {
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/generate") {
            val request = call.receive<SubmissionRequest>()
            val url = request.url

            val jobId = java.util.UUID.randomUUID().toString() // Placeholder for job ID
            println("Received URL: $url, Job ID: $jobId")

            val githubRequest = GitHubRequest(
                event_type = "build-app",
                client_payload = ClientPayload(
                    url = url,
                    package_name = "com.example.newapp"
                )
            )

            val githubResponse: HttpResponse =
                httpClient.post("https://api.github.com/repos/prudhvir3ddy/wtm/dispatches") {
                    header(HttpHeaders.Accept, "application/vnd.github+json")
                    header(
                        HttpHeaders.Authorization,
                        "Bearer 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"
                    )
                    contentType(ContentType.Application.Json)
                    setBody(
                        githubRequest
                    )
                }

            println("GitHub API Response: ${Json.encodeToString(githubRequest)} ${githubResponse}")

            call.respond(SubmissionResponse(jobId = jobId))
        }
    }
}
