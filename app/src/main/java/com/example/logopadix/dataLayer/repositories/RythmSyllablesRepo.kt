package com.example.logopadix.dataLayer.repositories

import android.content.Context
import com.example.logopadix.R
import com.example.logopadix.dataLayer.IModel
import com.example.logopadix.presentationLayer.screens.levelsScreen.IImageLevel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class RythmSyllablesRepo(ctx: Context):
    ResourceMappedRepository<RythmSyllabData, RythmSyllabRound>(
        ctx,
        R.raw.rythm_syllabels_data,
        RythmSyllabData::class.java
    )

class RythmSyllabRound: IImageLevel {
    @JacksonXmlProperty(isAttribute = true)
    override val imageName: String = ""
    @JacksonXmlProperty(isAttribute = true)
    val soundName: String = ""
    @JacksonXmlProperty(isAttribute = true)
    val syllabCount: Int = 0
}

@JacksonXmlRootElement(localName = "data")
data class RythmSyllabData(
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    override val data: List<RythmSyllabRound> = emptyList()
) : IModel<RythmSyllabRound>