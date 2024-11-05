package com.example.bakalarkaapp.dataLayer

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

class TextValue {
    @JacksonXmlText
    val value: String = ""
}