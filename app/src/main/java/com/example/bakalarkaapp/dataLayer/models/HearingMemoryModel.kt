package com.example.bakalarkaapp.dataLayer.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

/**
 * Represents a round of the Hearing Memory exercise.
 *
 * @property objects A list of [WordContent] items, which are used in the round.
 * @property toBePlayedCount A count of words that user has to memorize.
 */
data class HearingMemoryRound(
    @JacksonXmlProperty(localName = "roundContent")
    @JacksonXmlElementWrapper(useWrapping = false)
    val objects: List<WordContent> = emptyList(),
    val toBePlayedCount: Int = 0
)


/**
 * Represents multiple rounds for Hearing Memory exercise.
 *
 * @property rounds A list of [HearingMemoryRound] items, holding data for each round.
 */
data class HearingMemoryData(
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    val rounds: List<HearingMemoryRound> = emptyList()
)