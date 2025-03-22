package com.example.logopadix.dataLayer

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

/**
 * Represents content which has image, sound or text.
 *
 * @property imageName The name of the drawable resource file.
 * @property soundName The name of mp3 raw resource file.
 * @property text A text value of the content.
 */
class WordContent {
    @JacksonXmlProperty(isAttribute = true)
    val imageName: String? = null
    @JacksonXmlProperty(isAttribute = true)
    val soundName: String? = null
    @JacksonXmlText
    val text: String? = null
}