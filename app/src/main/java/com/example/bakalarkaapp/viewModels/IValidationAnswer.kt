package com.example.bakalarkaapp.viewModels

sealed interface IValidationAnswer {
    data class StringAnswer(val value: String): IValidationAnswer
    data class BooleanAnswer(val value: Boolean): IValidationAnswer
    data object BlankAnswer : IValidationAnswer
}