package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.LevelWithImage
import com.example.bakalarkaapp.utils.xml.XmlParser
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement


class EyesightComparisonRepo(context: Context): IRepository<ComparisonItem> {
    private val mappedClass = XmlParser.
    parseXmlData(context, R.xml.eyesight_comparison_data, ComparisonData::class.java)
    override val data = mappedClass.data
}

@JacksonXmlRootElement(localName = "comparisonItem")
class ComparisonItem: LevelWithImage {
    @JacksonXmlProperty(localName = "imageId")
    override val background = ""

    @JacksonXmlProperty(localName = "isSameShape")
    val isSameShape = true
}

@JacksonXmlRootElement(localName = "data")
class ComparisonData {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "comparisonItem")
    val data = emptyList<ComparisonItem>()
}

