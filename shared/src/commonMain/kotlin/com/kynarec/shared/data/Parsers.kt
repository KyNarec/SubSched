package com.kynarec.shared.data

import com.fleeksoft.ksoup.Ksoup
import com.kynarec.shared.data.models.Day
import com.kynarec.shared.data.models.Messages
import com.kynarec.shared.data.models.Substitution
import com.kynarec.shared.data.models.SubstitutionSchedule
import com.kynarec.shared.data.models.Teacher

fun parseFullTeacherSubstituteSchedule(html: String): SubstitutionSchedule {
    val doc = Ksoup.parse(html)

    val dailyContainers = doc.select(".daily_table")
    val message = doc.select("inner_news_container")
    val days = mutableListOf<Day>()

    for (container in dailyContainers) {
        val substitutions = mutableListOf<Substitution>()

        val dateHeader = container.select(".daily_date_hdl").text()

        // Target rows that actually contain data (those with specific classes)
        val rows = container.select("tr.liste_darkgrey, tr.liste_grau")

        var currentTeacher = ""
        for (row in rows) {
            val cols = row.select("td")
            if (cols.size < 6) continue

            // 1. Handle "Sticky" Teacher column
            val teacherRaw = cols[0].text().replace("\u00a0", "").trim() // handles &nbsp;
            if (teacherRaw.isNotEmpty()) {
                currentTeacher = teacherRaw
            }
//            currentTeacher = teacherRaw

            // 2. Parse the 'Abw.' column: "Str (D)" -> Teacher: Str, Subject: D
            val abwRaw = cols[3].text()
            val abwRegex = Regex("""(.+)\s\((.+)\)""")
            val match = abwRegex.find(abwRaw)

            val absentTeachers = match?.groupValues?.get(1) ?: abwRaw
            val subject = match?.groupValues?.get(2) ?: ""

            val entry = Substitution(
                coveringTeacher = Teacher(currentTeacher) ,
                lesson = cols[1].text().toIntOrNull() ?: 0,
                className = cols[2].text().trim(),
                absentTeacher = Teacher(absentTeachers.trim()),
                subject = subject.trim(),
                room = cols[4].text().trim().ifEmpty { "-" },
                info = cols[5].text().trim()
            )
            substitutions.add(entry)
        }
        days.add(
            Day(
                date = dateHeader,
                absentTeachers = substitutions.map { it.absentTeacher }.distinctBy { it.name },
                impactedTeachers = substitutions
                    .map { it.coveringTeacher }
                    .filter { teacher ->
                        teacher.name.isNotBlank() && teacher.name != "-"
                    }
                    .distinctBy { it.name },
                substitutions = substitutions
            )
        )
    }
    var date = ""
    val messages = mutableListOf<String>()
    for (msg in message) {
        val headerElement = doc.selectFirst(".msg_hdl") ?: continue

        // Extract the date using Regex (matches DD.MM.YYYY)
        val dateRegex = Regex("""\d{2}\.\d{2}\.\d{4}""")
        val dateText = headerElement.text()
        date = dateRegex.find(dateText)?.value ?: "Unknown Date"

        TODO("Extract individual messages")
    }
    return SubstitutionSchedule(
        days = days,
        messages = Messages(
            date = date,
            messages = messages
        )
    )
}