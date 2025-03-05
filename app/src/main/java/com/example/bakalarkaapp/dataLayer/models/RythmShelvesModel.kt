package com.example.bakalarkaapp.dataLayer.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement


class ShelvesRytmicWords {
    @JacksonXmlProperty(localName = "word")
    @JacksonXmlElementWrapper(useWrapping = false)
    val rytmicWords: List<WordContent> = emptyList()
}


class ShelvesRound {
    @JacksonXmlProperty(localName = "rhymes")
    @JacksonXmlElementWrapper(useWrapping = false)
    val rhymeSets: List<ShelvesRytmicWords> = emptyList()
}


@JacksonXmlRootElement(localName = "data")
class ShelvesRounds {
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    val rounds: List<ShelvesRound> = emptyList()
}