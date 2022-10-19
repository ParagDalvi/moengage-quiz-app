package com.moengage.machinecoding.models

data class QuizInfo(
    val questions: MutableList<Question> = mutableListOf()
)
