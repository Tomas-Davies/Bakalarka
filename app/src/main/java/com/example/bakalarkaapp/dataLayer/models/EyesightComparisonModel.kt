package com.example.bakalarkaapp.dataLayer.models


import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.ImageLevel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

/**
 * Represents an item for round of Eyesight Comparison exercise.
 *
 * Configured for Xml deserialization using JacksonXml annotations.
 *
 * @property imageName The name of the drawable resource file.
 * @property isSameShape A value determining if shapes visible on the drawable resource with *imageName* are identical.
 */
data class ComparisonItem(
    @JacksonXmlProperty(localName = "imageId")
    override val imageName: String = "",
    val isSameShape: Boolean = true
) : ImageLevel


/**
 * Represents data for Eyesight Comparison exercise.
 *
 * @property items A list of [ComparisonItem] items.
 */
@JacksonXmlRootElement(localName = "data")
data class ComparisonData(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "comparisonItem")
    val items: List<ComparisonItem> = emptyList()
)

