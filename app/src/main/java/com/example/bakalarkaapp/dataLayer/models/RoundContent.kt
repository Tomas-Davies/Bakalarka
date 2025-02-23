package com.example.bakalarkaapp.dataLayer.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

class RoundContent {
    @JacksonXmlProperty(isAttribute = true)
    val imgName: String? = null
    @JacksonXmlProperty(isAttribute = true)
    val soundName: String? = null
    @JacksonXmlText
    val text: String? = null
}