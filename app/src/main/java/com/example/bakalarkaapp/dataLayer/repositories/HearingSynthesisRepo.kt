package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.utils.xml.XmlParser
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

class HearingSynthesisRepo(ctx: Context) {
    private val mappedClass = XmlParser.
    parseXmlData(ctx, R.xml.hearing_synthesis_data, HearingSynthData::class.java)
    val data = mappedClass.data
}

class HearingSynthRound {
    @JacksonXmlProperty(localName = "word")
    @JacksonXmlElementWrapper(useWrapping = false)
    val words = emptyList<String>()
}

class HearingSynthData {
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    val data = emptyList<HearingSynthRound>()
}