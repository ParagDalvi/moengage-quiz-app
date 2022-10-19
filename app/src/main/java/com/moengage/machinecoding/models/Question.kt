package com.moengage.machinecoding.models

data class Question(
    val question: String,
    val type: String,
    var answer: String? = null
)
