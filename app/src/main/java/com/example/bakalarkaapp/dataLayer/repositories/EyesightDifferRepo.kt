package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.ImageLevel
import com.example.bakalarkaapp.utils.xml.XmlParser
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class EyesightDifferRepo(context: Context): IRepositoryWithImageLevels<DifferItem> {
    private val mappedClass = XmlParser.
        parseXmlData(context, R.xml.eyesight_differ_data, DifferData::class.java)
    override val data = mappedClass.data
}


@JacksonXmlRootElement(localName = "round")
class Round {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "question")
    val question = ""

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "correctAnswer")
    val answers = emptyList<String>()
}

@JacksonXmlRootElement(localName = "differItem")
class DifferItem: ImageLevel {
    @JacksonXmlProperty(localName = "imageId")
    override val background = ""

    @JacksonXmlProperty(localName = "rounds")
    val rounds = emptyList<Round>()
}

@JacksonXmlRootElement(localName = "data")
class DifferData{
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "differItem")
    val data = emptyList<DifferItem>()
}