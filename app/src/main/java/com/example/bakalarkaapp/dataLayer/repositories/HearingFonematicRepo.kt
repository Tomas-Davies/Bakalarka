package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.TextValue
import com.example.bakalarkaapp.utils.xml.XmlParser
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class HearingFonematicRepo(val context: Context) {
    private val mappedClass = XmlParser.
        parseXmlData(context, R.xml.hearing_fonematic_data, FonematicData::class.java)
    val data: List<FonematicRound> = mappedClass.data
}


@JacksonXmlRootElement(localName = "round")
data class FonematicRound(
    @JacksonXmlProperty(localName = "word")
    @JacksonXmlElementWrapper(useWrapping = false)
    val words: List<TextValue> = ArrayList()
)

@JacksonXmlRootElement(localName = "data")
data class FonematicData(
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    val data: List<FonematicRound> = ArrayList()
)