package com.example.bakalarkaapp.dataLayer.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

/**
 * Represents rounds which only use a set of drawable resource, sound resource and text per round.
 *
 * @property objects A list of [WordContent] items contained within this round.
 */
class BasicWordsRound(
    @JacksonXmlProperty(localName = "roundContent")
    @JacksonXmlElementWrapper(useWrapping = false)
    val objects: List<WordContent> = emptyList()
)

/**
 * Represents rounds used by basic exercises.
 *
 * @property data A list of [BasicWordsRound] items, holding data for each round.
 */

@JacksonXmlRootElement(localName = "data")
data class BasicWordsRounds(
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    override val data: List<BasicWordsRound> = emptyList()
) : IModel<BasicWordsRound>