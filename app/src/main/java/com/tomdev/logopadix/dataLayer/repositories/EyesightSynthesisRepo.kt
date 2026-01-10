package com.tomdev.logopadix.dataLayer.repositories

import android.content.Context
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.IData
import com.tomdev.logopadix.presentationLayer.screens.levels.IImageLevel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class EyesightSynthesisRepo(ctx: Context):
    ResourceMappedRepository<EyesightSynthData, EyesightSynthRound>(
        ctx,
        R.raw.eyesight_synth_data,
        EyesightSynthData::class.java
    )

class EyesightSynthRound : IImageLevel {
    @JacksonXmlProperty(localName = "imageName")
    override val imageName: String = ""
    @JacksonXmlProperty(isAttribute = true)
    override val difficulty: String = ""
    val pieceCount: Int = 0
}

@JacksonXmlRootElement(localName = "data")
data class EyesightSynthData (
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    override val data: List<EyesightSynthRound> = emptyList()
) : IData<EyesightSynthRound>