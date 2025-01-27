package com.example.bakalarkaapp.presentationLayer.states

data class AnswerResultState (
    val showResult: Boolean = false,
    var correctAnswer: Boolean = false,
    var message: String = ""
)