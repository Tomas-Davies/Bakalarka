package com.example.bakalarkaapp.dataLayer.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

/**
 * Data for Rythm Repeat exercise.
 *
 * @property soundNames A list of [String] names of mp3 raw resources.
 */
@JacksonXmlRootElement(localName = "data")
data class RythmRepeatData(
    @JacksonXmlProperty(localName = "soundName")
    @JacksonXmlElementWrapper(useWrapping = false)
    val soundNames: List<String> = emptyList()
)