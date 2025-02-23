package com.example.bakalarkaapp.dataLayer.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class HearingMemoryRound(
    @JacksonXmlProperty(localName = "roundContent")
    @JacksonXmlElementWrapper(useWrapping = false)
    val objects: List<RoundContent> = emptyList(),
    val toBePlayedCount: Int = 0
)

data class HearingMemoryData(
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    val rounds: List<HearingMemoryRound> = emptyList()
)