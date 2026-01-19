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

fun parseFullStudentSubstituteSchedule(html: String): SubstitutionSchedule {
    val doc = Ksoup.parse(html)

    val dailyContainers = doc.select(".daily_table")
    // Note: corrected selector to include the '#' for ID
    val messageContainer = doc.select("#inner_news_container")
    val days = mutableListOf<Day>()

    for (container in dailyContainers) {
        val substitutions = mutableListOf<Substitution>()
        val dateHeader = container.select(".daily_date_hdl").text()

        // Rows containing data
        val rows = container.select("tr.liste_darkgrey, tr.liste_grau")

        var currentClass = ""

        for (row in rows) {
            val cols = row.select("td")
            if (cols.size < 5) continue

            // 1. Handle "Sticky" Class column (Kl.)
            val classRaw = cols[0].text().replace("\u00a0", "").trim()
            if (classRaw.isNotEmpty()) {
                currentClass = classRaw
            }

            // 2. Parse the 'Ver.' column: "BrEv (E1_2)" or "(E1_2)"
            // Logic: If it contains parentheses, the part inside is the subject,
            // the part before is the covering teacher.
            val verRaw = cols[2].text().trim()
            val verRegex = Regex("""(?:(.+)\s)?\((.+)\)""")
            val match = verRegex.find(verRaw)

            val coveringTeacherName = match?.groupValues?.get(1)?.trim() ?: ""
            val subjectName = match?.groupValues?.get(2)?.trim() ?: verRaw

            val entry = Substitution(
                className = currentClass,
                lesson = cols[1].text().toIntOrNull() ?: 0,
                coveringTeacher = Teacher(coveringTeacherName),
                subject = subjectName,
                room = cols[3].text().replace("\u00a0", "").trim().ifEmpty { "-" },
                info = cols[4].text().trim(),
                // In student view, the "absent teacher" isn't explicitly listed
                // in its own column like the teacher view, often it's implied in info
                absentTeacher = Teacher("")
            )
            substitutions.add(entry)
        }

        days.add(
            Day(
                date = dateHeader,
                absentTeachers = emptyList(), // Student view doesn't provide a teacher list
                impactedTeachers = substitutions
                    .map { it.coveringTeacher }
                    .filter { it.name.isNotBlank() }
                    .distinctBy { it.name },
                substitutions = substitutions
            )
        )
    }

    // --- Message Parsing ---
    var messageDate = ""
    val messages = mutableListOf<String>()

    val headerElement = doc.selectFirst(".msg_hdl")
    if (headerElement != null) {
        val dateRegex = Regex("""\d{2}\.\d{2}\.\d{4}""")
        messageDate = dateRegex.find(headerElement.text())?.value ?: "Unknown Date"

        // Extract all <p> tags after the header or text lines within the container
        val pTags = messageContainer.select("p")
        pTags.forEach { p ->
            val text = p.text().trim()
            // Avoid adding the header itself to the message list
            if (text.isNotEmpty() && !text.contains("Nachrichten der Schulleitung")) {
                messages.add(text)
            }
        }
    }

    return SubstitutionSchedule(
        days = days,
        messages = Messages(
            date = messageDate,
            messages = messages
        )
    )
}