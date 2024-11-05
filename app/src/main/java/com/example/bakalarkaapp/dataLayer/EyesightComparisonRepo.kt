package com.example.bakalarkaapp.dataLayer

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.XmlUtils
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement


class EyesightComparisonRepo(context: Context) {
    private val mappedClass = XmlUtils.
    parseXmlData(context, R.xml.eyesight_comparison_data, ComparisonData::class.java)
    val data: List<ComparisonItem> = mappedClass.data
}

@JacksonXmlRootElement(localName = "comparisonItem")
class ComparisonItem {
    @JacksonXmlProperty(localName = "imageId")
    val imageId = TextValue()

    @JacksonXmlProperty(localName = "isSameShape")
    val isSameShape = TextValue()
}

@JacksonXmlRootElement(localName = "data")
class ComparisonData {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "comparisonItem")
    val data: List<ComparisonItem> = ArrayList()
}

