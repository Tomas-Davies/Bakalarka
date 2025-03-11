package com.example.bakalarkaapp.dataLayer.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement


@JacksonXmlRootElement(localName = "data")
data class RythmRepeatData(
    @JacksonXmlProperty(localName = "soundName")
    @JacksonXmlElementWrapper(useWrapping = false)
    override val data: List<String> = emptyList()
) : IModel<String>