package com.example.bakalarkaapp.dataLayer.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

class WordEntry {
    @JacksonXmlProperty(isAttribute = true)
    val imgName: String = ""
    @JacksonXmlProperty(isAttribute = true)
    val soundName: String = ""
    @JacksonXmlText
    val text: String = ""
}


data class LetterPosition(
    val label: String = "",
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "word")
    val words: List<WordEntry> = emptyList()
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
    val letters: List<SpeechLetter> = emptyList()
)