package com.tomdev.logopadix.dataLayer.repositories

import android.content.Context
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.IModel
import com.tomdev.logopadix.presentationLayer.screens.levels.IImageLevel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

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
    val answers: List<String> = emptyList(),
)

class ObjectAndImage {
    @JacksonXmlProperty(isAttribute = true)
    val imageName: String = ""
    @JacksonXmlText
    val objectName: String = ""
}

@JacksonXmlRootElement(localName = "differItem")
data class DifferItem(
    @JacksonXmlProperty(localName = "imageId")
    override val imageName: String = "",
    val objects: List<ObjectAndImage> = emptyList(),
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