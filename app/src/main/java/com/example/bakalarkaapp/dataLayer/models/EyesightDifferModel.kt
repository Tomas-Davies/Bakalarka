package com.example.bakalarkaapp.dataLayer.models

import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.ImageLevel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

/**
 * Represents a round of the Eyesight Differ exercise.
 *
 *
 * @property question A [String] representing a *question*.
 * @property answers A list of [String] *answers* to the question.
 */
@JacksonXmlRootElement(localName = "round")
data class Round(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "question")
    val question: String = "",
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "correctAnswer")
    val answers: List<String> = emptyList()
)

/**
 * Represents a differItem in the XML structure, holding drawable resource for set of rounds.
 *
 *
 * @property imageName The name of the drawable resource file.
 * @property rounds A list of [Round] items.
 */
@JacksonXmlRootElement(localName = "differItem")
data class DifferItem(
    @JacksonXmlProperty(localName = "imageId")
    override val imageName: String = "",
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "round")
    val rounds: List<Round> = emptyList()
) : ImageLevel

/**
 * Represents the root XML structure containing multiple rounds for Eyesight Differ exercises.
 *
 *
 * @property data A list of [BasicWordsRound] items, holding data for each round.
 */
@JacksonXmlRootElement(localName = "data")
data class DifferData(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "differItem")
    val data: List<DifferItem> = emptyList()
)