package com.example.logopadix.dataLayer.repositories

import android.content.Context
import com.example.logopadix.R
import com.example.logopadix.dataLayer.IModel
import com.example.logopadix.presentationLayer.screens.levelsScreen.IImageLevel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class EyesightDifferRepo(ctx: Context) :
    ResourceMappedRepository<DifferData, DifferItem>(
        ctx,
        R.raw.eyesight_differ_data,
        DifferData::class.java
    )

@JacksonXmlRootElement(localName = "round")
data class Round(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "question")
    val question: String = "",
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "correctAnswer")
    val answers: List<String> = emptyList()
)

@JacksonXmlRootElement(localName = "differItem")
data class DifferItem(
    @JacksonXmlProperty(localName = "imageId")
    override val imageName: String = "",
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "round")
    val rounds: List<Round> = emptyList()
) : IImageLevel

@JacksonXmlRootElement(localName = "data")
data class DifferData(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "differItem")
    override val data: List<DifferItem> = emptyList()
) : IModel<DifferItem>