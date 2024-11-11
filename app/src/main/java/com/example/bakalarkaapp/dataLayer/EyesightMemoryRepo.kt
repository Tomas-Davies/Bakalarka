package com.example.bakalarkaapp.dataLayer

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.XmlUtils
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class EyesightMemoryRepo(context: Context) {
    private val mappedClass = XmlUtils.
        parseXmlData(context, R.xml.eyesight_memory_data, MemoryData::class.java)
    val data: List<MemoryItem> = mappedClass.data
}

class MemoryItem {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "object")
    val objects: List<TextValue> = emptyList()
}

@JacksonXmlRootElement(localName = "data")
class MemoryData {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "round")
    val data: List<MemoryItem> = emptyList()
}