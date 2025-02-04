package com.example.ghosttest.models

data class Quiz(
    val question: String = "",
    val correctAnswer: String = "",
    val wrongAnswers: List<String> = emptyList(),
    val xpReward: Int = 50
)