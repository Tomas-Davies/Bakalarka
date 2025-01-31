package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.TextValue
import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.LevelWithImage
import com.example.bakalarkaapp.utils.xml.XmlParser
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class EyesightSearchRepo(context: Context): IRepository<SearchRound> {
    private val mappedClass = XmlParser.
    parseXmlData(context, R.xml.eyesight_search_data, SearchData::class.java)
    override val data: List<SearchRound> = mappedClass.data
}

@JacksonXmlRootElement(localName = "color")
class ItemColor {
    @JacksonXmlProperty(localName = "r")
    val r = TextValue()
    @JacksonXmlProperty(localName = "g")
    val g = TextValue()
    @JacksonXmlProperty(localName = "b")
    val b = TextValue()
}

@JacksonXmlRootElement(localName = "item")
class SearchItem {
    @JacksonXmlProperty(localName = "xPosPercentage")
    val x = TextValue()
    @JacksonXmlProperty(localName = "yPosPercentage")
    val y = TextValue()
    @JacksonXmlProperty(localName = "width")
    val width = TextValue()
    @JacksonXmlProperty(localName = "height")
    val height = TextValue()
    @JacksonXmlProperty(localName = "color")
    val color = ItemColor()
}

@JacksonXmlRootElement(localName = "round")
class SearchRound: LevelWithImage {
    @JacksonXmlProperty(localName = "background")
    override val background = TextValue()
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    val items : List<SearchItem> = emptyList()
}
@JacksonXmlRootElement(localName = "data")
class SearchData {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "round")
    val data: List<SearchRound> = emptyList()
}