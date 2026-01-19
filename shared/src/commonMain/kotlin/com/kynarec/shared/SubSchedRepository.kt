package com.kynarec.shared

import eu.anifantakis.lib.ksafe.KSafe
import io.ktor.client.HttpClient
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

class SubSchedRepository(private val client: HttpClient, private val kSafeInstance: KSafe) {

    suspend fun fetchTeacherInfo(
        username: String,
        password: String,
        days: Int = 2,
        type: String = "teacher"
    ): String {
        return try {
            val response = client.get("https://schule-infoportal.de/infoscreen/") {
                url {
                    parameter("type", type)
                    parameter("refresh", "100")
                    parameter("fontsize", "18")
                    parameter("days", days.toString())
                    parameter("future", "0")
                    parameter("theme", "light")
                    parameter("news", "")
                    parameter("ticker", "ende/")
                }

                basicAuth(username, password)
            }

            if (response.status == HttpStatusCode.OK) {
                response.bodyAsText()
            } else {
                println("Request failed with status: ${response.status}")
                ""
            }
        } catch (e: Exception) {
            println("Network Error: ${e.message}")
            ""
        }
    }

    val kSafe : KSafe
        get() = kSafeInstance
}