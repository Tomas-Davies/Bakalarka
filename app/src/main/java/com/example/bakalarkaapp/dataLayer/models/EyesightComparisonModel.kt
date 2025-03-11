package com.example.bakalarkaapp.dataLayer.models


import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.IImageLevel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement


data class ComparisonItem(
    override val imageName: String = "",
    val isSameShape: Boolean = true
) : IImageLevel


@JacksonXmlRootElement(localName = "data")
data class ComparisonData(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "comparisonItem")
    override val data: List<ComparisonItem> = emptyList()
) : IModel<ComparisonItem>

