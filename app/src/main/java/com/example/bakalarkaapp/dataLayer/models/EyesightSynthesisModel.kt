package com.example.bakalarkaapp.dataLayer.models

import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.ImageLevel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

/**
 * Represents a round of the Eyesight Synthesis exercise.
 *
 * @property imageName The name of the drawable resource file.
 * @property pieceCount A number of bitmap pieces from *imageName* bitmap.
 */
data class EyesightSynthRound(
    @JacksonXmlProperty(localName = "imageName")
    override val imageName: String = "",
    val pieceCount: Int = 0
): ImageLevel


/**
 * Represents multiple rounds for Eyesight Synthesis exercises.
 *
 * @property rounds A list of [EyesightSynthRound] items, holding data for each round.
 */
@JacksonXmlRootElement(localName = "data")
data class EyesightSynthData (
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    val rounds: List<EyesightSynthRound> = emptyList()
)