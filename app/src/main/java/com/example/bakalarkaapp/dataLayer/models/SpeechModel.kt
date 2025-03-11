package com.example.bakalarkaapp.dataLayer.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement


data class LetterPosition(
    val label: String = "",
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "word")
    val words: List<WordContent> = emptyList()
)


data class SpeechLetter(
    val label: String = "",
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "position")
    val positions: List<LetterPosition> = emptyList()
){
    val isPrimitive = positions.isNotEmpty() && positions[0].label == "NONE"
}


@JacksonXmlRootElement(localName = "letters")
data class SpeechData(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "letter")
    override val data: List<SpeechLetter> = emptyList()
) : IModel<SpeechLetter>