package com.example.bakalarkaapp.presentationLayer.states

data class ResultMessageState (
    val showMessage: Boolean = false,
    var correctAnswer: Boolean = false,
    var message: String = ""
)