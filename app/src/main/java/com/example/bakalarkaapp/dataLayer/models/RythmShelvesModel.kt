package com.example.bakalarkaapp.dataLayer.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement


data class ShelvesRytmicWords (
    @JacksonXmlProperty(localName = "word")
    @JacksonXmlElementWrapper(useWrapping = false)
    val rytmicWords: List<WordContent> = emptyList()
)


data class ShelvesRound (
    @JacksonXmlProperty(localName = "rhymes")
    @JacksonXmlElementWrapper(useWrapping = false)
    val rhymeSets: List<ShelvesRytmicWords> = emptyList()
)


@JacksonXmlRootElement(localName = "data")
data class ShelvesRounds (
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    override val data: List<ShelvesRound> = emptyList()
) : IModel<ShelvesRound>