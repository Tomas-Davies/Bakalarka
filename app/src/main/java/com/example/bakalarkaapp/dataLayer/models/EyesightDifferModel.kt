package com.example.bakalarkaapp.dataLayer.models

import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.ImageLevel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement


@JacksonXmlRootElement(localName = "round")
data class Round(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "question")
    val question: String = "",
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "correctAnswer")
    val answers: List<String> = emptyList()
)


@JacksonXmlRootElement(localName = "differItem")
data class DifferItem(
    @JacksonXmlProperty(localName = "imageId")
    override val imageName: String = "",
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "round")
    val rounds: List<Round> = emptyList()
) : ImageLevel


@JacksonXmlRootElement(localName = "data")
data class DifferData(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "differItem")
    val data: List<DifferItem> = emptyList()
)