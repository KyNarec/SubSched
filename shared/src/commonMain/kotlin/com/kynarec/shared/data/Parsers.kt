package com.kynarec.shared.data

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.TextNode
import com.kynarec.shared.data.models.Day
import com.kynarec.shared.data.models.Messages
import com.kynarec.shared.data.models.Substitution
import com.kynarec.shared.data.models.SubstitutionSchedule
import com.kynarec.shared.data.models.Teacher

fun parseFullTeacherSubstituteSchedule(html: String): SubstitutionSchedule {
    val doc = Ksoup.parse(html)

    val dailyContainers = doc.select(".daily_table")
    val messageContainer = doc.select("#inner_news_container")
    val days = mutableListOf<Day>()

    for (container in dailyContainers) {
        val substitutions = mutableListOf<Substitution>()
        val dateHeader = container.select(".daily_date_hdl").text()

        val rows = container.select("tr.liste_darkgrey, tr.liste_grau")

        var currentTeacher = ""
        for (row in rows) {
            val cols = row.select("td")
            if (cols.size < 6) continue

            val teacherRaw = cols[0].text().replace("\u00a0", "").trim()
            if (teacherRaw.isNotEmpty() && teacherRaw != "-") {
                currentTeacher = teacherRaw
            }

            val abwRaw = cols[3].text()
            val abwRegex = Regex("""(.+)\s\((.+)\)""")
            val match = abwRegex.find(abwRaw)

            val absentTeachers = match?.groupValues?.get(1) ?: abwRaw
            val subject = match?.groupValues?.get(2) ?: ""

            val entry = Substitution(
                coveringTeacher = Teacher(currentTeacher),
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

    var messageDate = ""
    val messages = mutableListOf<String>()

    val headerElement = doc.selectFirst(".msg_hdl")
    if (headerElement != null) {
        val dateRegex = Regex("""\d{2}\.\d{2}\.\d{4}""")
        messageDate = dateRegex.find(headerElement.text())?.value ?: "Unknown Date"

        val newsBlocks = messageContainer.select(".news")
        for (block in newsBlocks) {
            val headline1 = block.select(".news_headline_1").text().trim()
            val headline2 = block.select(".news_headline_2").text().trim()

            val bodyElement = block.selectFirst(".news_text")

            val bodyText = bodyElement?.childNodes()?.joinToString("") { node ->
                when (node) {
                    is TextNode -> node.text()
                    is Element -> {
                        if (node.tagName() == "br") {
                            "\n"
                        } else {
                            node.text()
                        }
                    }
                    else -> ""
                }
            }?.trim() ?: ""

            val combinedMessage = buildString {
                if (headline1.isNotEmpty()) append("$headline1\n")
                if (headline2.isNotEmpty()) append("$headline2\n")
                append(bodyText)
            }.trim()

            if (combinedMessage.isNotEmpty()) {
                messages.add(combinedMessage)
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

fun parseFullStudentSubstituteSchedule(html: String): SubstitutionSchedule {
    val doc = Ksoup.parse(html)

    val dailyContainers = doc.select(".daily_table")
    val messageContainer = doc.select("#inner_news_container")
    val days = mutableListOf<Day>()

    for (container in dailyContainers) {
        val substitutions = mutableListOf<Substitution>()
        val dateHeader = container.select(".daily_date_hdl").text()
        val rows = container.select("tr.liste_darkgrey, tr.liste_grau")

        var currentClass = ""

        for (row in rows) {
            val cols = row.select("td")
            if (cols.size < 5) continue

            val classRaw = cols[0].text().replace("\u00a0", "").trim()
            if (classRaw.isNotEmpty()) {
                currentClass = classRaw
            }

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
                absentTeacher = Teacher("")
            )
            substitutions.add(entry)
        }

        days.add(
            Day(
                date = dateHeader,
                absentTeachers = emptyList(),
                impactedTeachers = substitutions
                    .map { it.coveringTeacher }
                    .filter { it.name.isNotBlank() }
                    .distinctBy { it.name },
                substitutions = substitutions
            )
        )
    }

    var messageDate = ""
    val messages = mutableListOf<String>()

    val headerElement = doc.selectFirst(".msg_hdl")
    if (headerElement != null) {
        val dateRegex = Regex("""\d{2}\.\d{2}\.\d{4}""")
        messageDate = dateRegex.find(headerElement.text())?.value ?: "Unknown Date"

        val newsBlocks = messageContainer.select(".news")
        for (block in newsBlocks) {
            val headline1 = block.select(".news_headline_1").text().trim()
            val headline2 = block.select(".news_headline_2").text().trim()

            val bodyElement = block.selectFirst(".news_text")

            val bodyText = bodyElement?.childNodes()?.joinToString("") { node ->
                when (node) {
                    is TextNode -> node.text()
                    is Element -> {
                        if (node.tagName() == "br") {
                            "\n"
                        } else {
                            node.text()
                        }
                    }
                    else -> ""
                }
            }?.trim() ?: ""

            val combinedMessage = buildString {
                if (headline1.isNotEmpty()) append("$headline1\n")
                if (headline2.isNotEmpty()) append("$headline2\n")
                append(bodyText)
            }.trim()

            if (combinedMessage.isNotEmpty()) {
                messages.add(combinedMessage)
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