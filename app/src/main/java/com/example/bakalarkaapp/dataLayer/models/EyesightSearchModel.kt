package com.example.bakalarkaapp.dataLayer.models

import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.ImageLevel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

data class ItemColor(
    val r: Int = 0,
    val g: Int = 0,
    val b: Int = 0
)

data class SearchItem(
    @JacksonXmlProperty(localName = "xPosPercentage")
    val xPerc: Float = 0f,
    @JacksonXmlProperty(localName = "yPosPercentage")
    val yPerc: Float = 0f,
    @JacksonXmlProperty(localName = "widthPercentage")
    val widthPerc: Float = 0f,
    @JacksonXmlProperty(localName = "heightPercentage")
    val heightPerc: Float = 0f,
    val color: ItemColor = ItemColor()
)

data class SearchRound(
    @JacksonXmlProperty(localName = "background")
    override val imageName: String = "",
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    val items: List<SearchItem> = emptyList()
) : ImageLevel

@JacksonXmlRootElement(localName = "data")
data class SearchData(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "round")
    val rounds: List<SearchRound> = emptyList()
)