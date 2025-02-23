package com.example.bakalarkaapp.dataLayer.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class BasicWordsRound(
    @JacksonXmlProperty(localName = "roundContent")
    @JacksonXmlElementWrapper(useWrapping = false)
    val objects: List<RoundContent> = emptyList()
)

@JacksonXmlRootElement(localName = "data")
data class BasicWords(
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    val rounds: List<BasicWordsRound> = emptyList()
)