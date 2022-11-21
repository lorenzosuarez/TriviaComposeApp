package com.example.triviaapp.domain.model

data class QuestionsResponseItem(
    val category: String,
    val correctAnswer: String,
    val difficulty: String,
    val id: String,
    val incorrectAnswers: List<String>,
    val question: String,
    val regions: List<Any>,
    val tags: List<String>,
    val type: String
)