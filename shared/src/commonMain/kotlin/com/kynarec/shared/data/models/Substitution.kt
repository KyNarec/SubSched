package com.kynarec.shared.data.models

data class Substitution(
    val coveringTeacher: Teacher,
    val lesson: Int,
    val className: String,
    val absentTeacher: Teacher,
    val subject: String,
    val room: String = "-",
    val info: String,
)
