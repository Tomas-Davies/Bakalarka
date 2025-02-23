package com.example.bakalarkaapp.dataLayer.models

import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.ImageLevel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

data class EyesightSynthRound(
    @JacksonXmlProperty(localName = "imageName")
    override val imageName: String = "",
    val pieceCount: Int = 0
): ImageLevel


@JacksonXmlRootElement(localName = "data")
data class EyesightSynthData (
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    val rounds: List<EyesightSynthRound> = emptyList()
)