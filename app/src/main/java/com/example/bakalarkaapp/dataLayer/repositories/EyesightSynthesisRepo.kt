package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.TextValue
import com.example.bakalarkaapp.utils.xml.XmlParser
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class EyesightSynthesisRepo(context: Context) {
    private val mappedClass = XmlParser.
    parseXmlData(context, R.xml.eyesight_synth_data, SynthData::class.java)
    val data: List<SynthRound> = mappedClass.data
}


@JacksonXmlRootElement(localName = "round")
data class SynthRound(
    @JacksonXmlProperty(localName = "image")
    val image: TextValue,
    @JacksonXmlProperty(localName = "pieceCount")
    val pieceCount: Int
)

@JacksonXmlRootElement(localName = "data")
data class SynthData(
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    val data: List<SynthRound> = ArrayList()
)