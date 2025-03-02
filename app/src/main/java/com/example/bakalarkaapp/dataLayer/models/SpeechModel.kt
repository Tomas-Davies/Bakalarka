package com.example.bakalarkaapp.dataLayer.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

/**
 * Describes a position of the letter in a word and provides these words.
 *
 * @property label A label of the position.
 * @property words A list of [WordContent]
 */
data class LetterPosition(
    val label: String = "",
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "word")
    val words: List<WordContent> = emptyList()
)

/**
 * Represents a Letter for Speech exercise.
 *
 * @property label A label describing the letter.
 * @property positions A list of [LetterPosition].
 */
data class SpeechLetter(
    val label: String = "",
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "position")
    val positions: List<LetterPosition> = emptyList()
){
    val isPrimitive = positions.isNotEmpty() && positions[0].label == "NONE"
}


/**
 * Represents data for Speech exercise.
 *
 * @property letters A list of [SpeechLetter] providing additional letter data.
 */
@JacksonXmlRootElement(localName = "letters")
data class SpeechData(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "letter")
    val letters: List<SpeechLetter> = emptyList()
)