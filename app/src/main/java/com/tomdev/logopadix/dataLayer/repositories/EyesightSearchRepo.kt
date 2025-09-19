package com.tomdev.logopadix.dataLayer.repositories

import android.content.Context
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.IModel
import com.tomdev.logopadix.presentationLayer.screens.levels.IImageLevel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement


class EyesightSearchRepo(ctx: Context) :
    ResourceMappedRepository<SearchData, SearchRound>(
        ctx,
        R.raw.eyesight_search_data,
        SearchData::class.java
    )

/**
 * Data for overlay component used on top of *Image*.
 *
 * The coordinates and size of the overlay are expressed as percentages relative to *Image* size,
 * making it more intuitive and making them adapt to any size and image resolution as long as the *Image* aspect ratio is 1f
 *
 * @property xPerc The overlays center x-coordinate expressed as percentage (0-100) of *Image* width.
 * @property yPerc The overlays center y-coordinate expressed as percentage (0-100) of *Image* height.
 * @property widthPerc The width of the overlay, expressed as percentage (0-100) of *Image* width.
 * @property heightPerc The height of the overlay, expressed as percentage (0-100) of *Image* height.
 */
data class SearchItemOverlay(
    @JacksonXmlProperty(localName = "xPosPercentage")
    val xPerc: Float = 0f,
    @JacksonXmlProperty(localName = "yPosPercentage")
    val yPerc: Float = 0f,
    @JacksonXmlProperty(localName = "widthPercentage")
    val widthPerc: Float = 0f,
    @JacksonXmlProperty(localName = "heightPercentage")
    val heightPerc: Float = 0f
)

/**
 * Represents a round of the Eyesight Search exercise.
 *
 * @property imageName The name of the drawable resource file containing image used for searching objects hidden by overlays.
 * @property coloredImageName The name of the drawable resource file containing image used for searching objects hidden by overlays.
 * @property items A list of [SearchItemOverlay] items.
 */
data class SearchRound(
    @JacksonXmlProperty(localName = "backgroundNonColored")
    override val imageName: String = "",
    @JacksonXmlProperty(localName = "backgroundColored")
    val coloredImageName: String = "",
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    val items: List<SearchItemOverlay> = emptyList()
) : IImageLevel

@JacksonXmlRootElement(localName = "data")
data class SearchData(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "round")
    override val data: List<SearchRound> = emptyList()
) : IModel<SearchRound>