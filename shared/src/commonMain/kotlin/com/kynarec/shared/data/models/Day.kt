package com.kynarec.shared.data.models

data class Day(
    val date: String,
    val absentTeachers: List<Teacher>,
    val impactedTeachers: List<Teacher>,
    val substitutions: List<Substitution>,
)
