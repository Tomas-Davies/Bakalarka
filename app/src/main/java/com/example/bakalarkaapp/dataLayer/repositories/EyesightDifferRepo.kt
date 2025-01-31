package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.TextValue
import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.LevelWithImage
import com.example.bakalarkaapp.utils.xml.XmlParser
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class EyesightDifferRepo(context: Context): IRepository<DifferItem> {
    private val mappedClass = XmlParser.
        parseXmlData(context, R.xml.eyesight_differ_data, DifferData::class.java)
    override val data: List<DifferItem> = mappedClass.data
}


@JacksonXmlRootElement(localName = "round")
class Round {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "question")
    val question = TextValue()

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "correctAnswer")
    val answers: List<TextValue> = ArrayList()
}

@JacksonXmlRootElement(localName = "differItem")
class DifferItem: LevelWithImage {
    @JacksonXmlProperty(localName = "imageId")
    override val background = TextValue()

    @JacksonXmlProperty(localName = "rounds")
    val rounds: List<Round> = ArrayList()
}

@JacksonXmlRootElement(localName = "data")
class DifferData{
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "differItem")
    val data: List<DifferItem> = ArrayList()
}