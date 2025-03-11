package com.example.bakalarkaapp.dataLayer.models

import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.IImageLevel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement


class RythmSyllabRound: IImageLevel {
    @JacksonXmlProperty(isAttribute = true)
    override val imageName: String = ""
    @JacksonXmlProperty(isAttribute = true)
    val soundName: String = ""
    @JacksonXmlProperty(isAttribute = true)
    val syllabCount: Int = 0
}


@JacksonXmlRootElement(localName = "data")
data class RythmSyllabData(
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    override val data: List<RythmSyllabRound> = emptyList()
) : IModel<RythmSyllabRound>