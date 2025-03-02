package com.example.bakalarkaapp.dataLayer.models

import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.ImageLevel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

/**
 * Represents a round of the Rythm Syllables exercise.
 *
 * @property imageName The name of the drawable resource file.
 * @property soundName The name of mp3 raw resource file.
 * @property syllabCount A count of syllables in word described by image and sound.
 */
class RythmSyllabRound: ImageLevel {
    @JacksonXmlProperty(isAttribute = true)
    override val imageName: String = ""
    @JacksonXmlProperty(isAttribute = true)
    val soundName: String = ""
    @JacksonXmlProperty(isAttribute = true)
    val syllabCount: Int = 0
}

/**
 * Represents multiple rounds for Rythm Syllables exercises.
 *
 * @property rounds A list of [RythmSyllabRound] items, holding data for each round.
 */
@JacksonXmlRootElement(localName = "data")
data class RythmSyllabData(
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    val rounds: List<RythmSyllabRound> = emptyList()
)