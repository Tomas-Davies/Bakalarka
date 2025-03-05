package com.example.bakalarkaapp.dataLayer.models


import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.ImageLevel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement


data class ComparisonItem(
    override val imageName: String = "",
    val isSameShape: Boolean = true
) : ImageLevel


@JacksonXmlRootElement(localName = "data")
data class ComparisonData(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "comparisonItem")
    val items: List<ComparisonItem> = emptyList()
)

