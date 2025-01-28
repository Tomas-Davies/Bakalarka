package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.TextValue
import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.LevelWithImage
import com.example.bakalarkaapp.utils.xml.XmlParser
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement


class EyesightComparisonRepo(context: Context): Repository<ComparisonItem> {
    private val mappedClass = XmlParser.
    parseXmlData(context, R.xml.eyesight_comparison_data, ComparisonData::class.java)
    override val data: List<ComparisonItem> = mappedClass.data
}

@JacksonXmlRootElement(localName = "comparisonItem")
class ComparisonItem: LevelWithImage {
    @JacksonXmlProperty(localName = "imageId")
    override val background = TextValue()

    @JacksonXmlProperty(localName = "isSameShape")
    val isSameShape = TextValue()
}

@JacksonXmlRootElement(localName = "data")
class ComparisonData {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "comparisonItem")
    val data: List<ComparisonItem> = ArrayList()
}

