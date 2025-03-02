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

/**
 * Data for overlay component used on top of *Image*. The overlay filters *color* under it to white (hiding the colored object).
 *
 * The coordinates and size of the overlay are expressed as percentages relative to *Image* size,
 * making it more intuitive and making them adapt to any size as long as the *Image* aspect ratio is 1f
 *
 * @property xPerc The overlays center x-coordinate expressed as percentage (0-100) of *Image* width.
 * @property yPerc The overlays center y-coordinate expressed as percentage (0-100) of *Image* height.
 * @property widthPerc The width of the overlay, expressed as percentage (0-100) of *Image* width.
 * @property heightPerc The height of the overlay, expressed as percentage (0-100) of *Image* height.
 * @property color The [ItemColor], which is going to be filtered to white.
 */
data class SearchItemOverlay(
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


/**
 * Represents a round of the Eyesight Search exercise.
 *
 * @property imageName The name of the drawable resource file containing image used for searching objects hidden by overlays.
 * @property items A list of [SearchItemOverlay] items.
 */
data class SearchRound(
    @JacksonXmlProperty(localName = "background")
    override val imageName: String = "",
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    val items: List<SearchItemOverlay> = emptyList()
) : ImageLevel


/**
 * Represents the root XML structure containing multiple rounds for Eyesight Search exercises.
 *
 * @property rounds A list of [SearchRound] items, holding data for each round.
 */
@JacksonXmlRootElement(localName = "data")
data class SearchData(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "round")
    val rounds: List<SearchRound> = emptyList()
)