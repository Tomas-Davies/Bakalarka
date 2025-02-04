package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.utils.xml.XmlParser
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class EyesightMemoryRepo(context: Context) {
    private val mappedClass = XmlParser.
        parseXmlData(context, R.xml.eyesight_memory_data, MemoryData::class.java)
    val data: List<MemoryItem> = mappedClass.data
}

class MemoryItem {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "object")
    val objects = emptyList<String>()
}

@JacksonXmlRootElement(localName = "data")
class MemoryData {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "round")
    val data = emptyList<MemoryItem>()
}