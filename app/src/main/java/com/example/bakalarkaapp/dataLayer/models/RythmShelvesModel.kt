package com.example.bakalarkaapp.dataLayer.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

/**
 * Represent words that rhyme with each other.
 *
 * @property rytmicWords A list of [WordContent] holding the words.
 */
class ShelvesRytmicWords {
    @JacksonXmlProperty(localName = "word")
    @JacksonXmlElementWrapper(useWrapping = false)
    val rytmicWords: List<WordContent> = emptyList()
}

/**
 * Represents a round of the Rythm Shelves exercise.
 *
 * @property rhymeSets A list of [ShelvesRytmicWords].
 */
class ShelvesRound {
    @JacksonXmlProperty(localName = "rhymes")
    @JacksonXmlElementWrapper(useWrapping = false)
    val rhymeSets: List<ShelvesRytmicWords> = emptyList()
}

/**
 * Represents multiple rounds for Rythm shelves exercise.
 *
 * @property rounds A list of [ShelvesRound] items, holding data for each round.
 */
@JacksonXmlRootElement(localName = "data")
class ShelvesRounds {
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    val rounds: List<ShelvesRound> = emptyList()
}