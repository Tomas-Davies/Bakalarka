package com.example.bakalarkaapp.dataLayer.models


sealed interface TaleContent {
    data class Word(val value: String): TaleContent
    data class Image(val imageName: String, val soundName: String): TaleContent
}

data class Image(
    val imageName: String = "",
    val soundName: String = ""
)

data class Tale(
    val name: String = "",
    val taleImageName: String = "",
    val content: List<Any> = emptyList()
)