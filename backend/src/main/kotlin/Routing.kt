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
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ReceivedPayload(
    val url: String,
    @SerialName("package_name")
    val packageName: String,
    val secret: String
)

@Serializable
data class ClientPayload(
    val url: String,
    @SerialName("package_name")
    val packageName: String
)

@Serializable
data class GitHubRequest(
    @SerialName("event_type") val eventType: String,
    @SerialName("client_payload") val clientPayload: ClientPayload
)

@Serializable
data class ErrorMessage(
    val message: String
)

private val GITHUB_TOKEN = System.getenv("GITHUB_TOKEN")
private val APP_SECRET = System.getenv("APP_SECRET")

fun Application.configureRouting() {
    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHost("localhost:8080", schemes = listOf("http"))
        allowHost("prudhvir3ddy.github.io", schemes = listOf("https"))
    }

    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    routing {
        get("/") {
            call.respondText("Yeeah! i'm up 🥱")
        }
        post("/generate") {
            val request = call.receive<ReceivedPayload>()
            val url = request.url
            val packageName = request.packageName
            val secret = request.secret

            if (secret != APP_SECRET) {
                call.respond(HttpStatusCode.Unauthorized, ErrorMessage("Invalid secret"))
                return@post
            }

            val githubRequest = GitHubRequest(
                eventType = "build-app",
                clientPayload = ClientPayload(
                    url = url,
                    packageName = packageName
                )
            )

            val githubResponse: HttpResponse =
                httpClient.post("https://api.github.com/repos/prudhvir3ddy/web2mob/dispatches") {
                    header(HttpHeaders.Accept, "application/vnd.github+json")
                    header(
                        HttpHeaders.Authorization,
                        "Bearer $GITHUB_TOKEN"
                    )
                    contentType(ContentType.Application.Json)
                    setBody(
                        githubRequest
                    )
                }

            println("GitHub API Response: $githubResponse")

            if (githubResponse.status.isSuccess()) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.ExpectationFailed)
            }
        }
    }
}
