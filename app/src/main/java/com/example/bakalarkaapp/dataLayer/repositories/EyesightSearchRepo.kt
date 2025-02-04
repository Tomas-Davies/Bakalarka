package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.LevelWithImage
import com.example.bakalarkaapp.utils.xml.XmlParser
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class EyesightSearchRepo(context: Context): IRepository<SearchRound> {
    private val mappedClass = XmlParser.
    parseXmlData(context, R.xml.eyesight_search_data, SearchData::class.java)
    override val data = mappedClass.data
}

@JacksonXmlRootElement(localName = "color")
class ItemColor {
    @JacksonXmlProperty(localName = "r")
    val r: Int = 0
    @JacksonXmlProperty(localName = "g")
    val g: Int = 0
    @JacksonXmlProperty(localName = "b")
    val b: Int = 0
}

@JacksonXmlRootElement(localName = "item")
class SearchItem {
    @JacksonXmlProperty(localName = "xPosPercentage")
    val x: Float = 0f
    @JacksonXmlProperty(localName = "yPosPercentage")
    val y: Float = 0f
    @JacksonXmlProperty(localName = "width")
    val width: Int = 0
    @JacksonXmlProperty(localName = "height")
    val height: Int = 0
    @JacksonXmlProperty(localName = "color")
    val color = ItemColor()
}

@JacksonXmlRootElement(localName = "round")
class SearchRound: LevelWithImage {
    @JacksonXmlProperty(localName = "background")
    override val background = ""
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    val items = emptyList<SearchItem>()
}
@JacksonXmlRootElement(localName = "data")
class SearchData {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "round")
    val data = emptyList<SearchRound>()
}