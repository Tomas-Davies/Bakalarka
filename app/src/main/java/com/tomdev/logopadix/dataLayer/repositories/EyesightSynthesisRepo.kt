package com.tomdev.logopadix.dataLayer.repositories

import android.content.Context
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.IModel
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

data class EyesightSynthRound(
    @JacksonXmlProperty(localName = "imageName")
    override val imageName: String = "",
    val pieceCount: Int = 0
): IImageLevel

@JacksonXmlRootElement(localName = "data")
data class EyesightSynthData (
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    override val data: List<EyesightSynthRound> = emptyList()
) : IModel<EyesightSynthRound>