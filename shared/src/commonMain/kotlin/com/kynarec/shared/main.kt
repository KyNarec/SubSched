package com.kynarec.shared

import com.kynarec.shared.data.parseFullSubstituteSchedule
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText


suspend fun main() {
    val response = SubstitutionSchedule().fetchTeacherInfo(
        username = "X",
        password = "X"
    )

    if (!response.isEmpty()) {

        val substitutionSchedule = parseFullSubstituteSchedule(response)
        println("======== FULL PLAN ========")
        substitutionSchedule.days.forEach { day ->
            println("Date: ${day.date}")
            println("Absent Teachers: ${day.absentTeachers.map { it.name }}")
            println("Affected Teachers: ${day.impactedTeachers.map { it.name }}")
            println("Substitutions:")
            day.substitutions.forEach { substitution ->
                println("  - ${substitution.coveringTeacher.name} | Lesson: ${substitution.lesson} | Class: ${substitution.className} | Absent: ${substitution.absentTeacher.name} | Subject: ${substitution.subject} | Room: ${substitution.room} | Info: ${substitution.info}")
            }
            println()
        }
        println(substitutionSchedule.messages.date + ": " + substitutionSchedule.messages.messages.joinToString("; "))
    }
}
