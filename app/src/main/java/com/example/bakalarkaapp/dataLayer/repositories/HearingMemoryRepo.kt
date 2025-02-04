package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.utils.xml.XmlParser
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

class HearingMemoryRepo(ctx: Context) {
    private val mappedClass = XmlParser.
        parseXmlData(ctx, R.xml.hearing_memory_data, HearingMemoryData::class.java)
    val data = mappedClass.data
}

class HearingMemoryRound {
    @JacksonXmlProperty(localName = "word")
    @JacksonXmlElementWrapper(useWrapping = false)
    val words = emptyList<String>()
    val toBePlayedCount: Int = 0
}

class HearingMemoryData {
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    val data = emptyList<HearingMemoryRound>()
}