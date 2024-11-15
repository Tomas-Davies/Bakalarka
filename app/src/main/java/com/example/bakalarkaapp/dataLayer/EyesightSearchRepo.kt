package com.example.bakalarkaapp.dataLayer

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.XmlUtils
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class EyesightSearchRepo(context: Context) {
    private val mappedClass = XmlUtils.
    parseXmlData(context, R.xml.eyesight_search_data, SearchData::class.java)
    val data: List<SearchRound> = mappedClass.data
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
    @JacksonXmlProperty(localName = "size")
    val size = TextValue()
    @JacksonXmlProperty(localName = "color")
    val color = ItemColor()
}

@JacksonXmlRootElement(localName = "round")
class SearchRound {
    @JacksonXmlProperty(localName = "background")
    val background = TextValue()
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