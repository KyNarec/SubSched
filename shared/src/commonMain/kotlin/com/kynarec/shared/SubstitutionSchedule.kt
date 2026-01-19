package com.kynarec.shared

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

class SubstitutionSchedule {
    suspend fun fetchTeacherInfo(
        username: String,
        password: String,
        days: Int = 2,
        type: String = "teacher"
    ): String {
        val client = HttpClient(CIO)

        try {
            val response: HttpResponse = client.get("https://schule-infoportal.de/infoscreen/") {
                // 1. Query Parameter
                url {
//                parameters.append("type", "student")
                    parameters.append("type", type)
                    parameters.append("refresh", "100")
                    parameters.append("fontsize", "18")
                    parameters.append("days", days.toString())
                    parameters.append("future", "0")
                    parameters.append("theme", "light")
                    parameters.append("news", "")
                    parameters.append("ticker", "ende/")
                }

                // 2. Standard Header
                headers {
                    basicAuth(username, password)

                }
            }
            println("Status: ${response.status}")
            println("Body: ${response.bodyAsText()}")
            return response.bodyAsText()
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return ""
        } finally {
            client.close()
        }
    }
}