package com.example.logopadix.dataLayer.repositories

import android.content.Context
import com.example.logopadix.R
import com.example.logopadix.dataLayer.IModel
import com.example.logopadix.dataLayer.WordContent
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class RythmShelvesRepo(ctx: Context) :
    ResourceMappedRepository<ShelvesRounds, ShelvesRound>(
        ctx,
        R.raw.rythm_shelves_data,
        ShelvesRounds::class.java
    )

data class ShelvesRytmicWords (
    @JacksonXmlProperty(localName = "word")
    @JacksonXmlElementWrapper(useWrapping = false)
    val rytmicWords: List<WordContent> = emptyList()
)

data class ShelvesRound (
    @JacksonXmlProperty(localName = "rhymes")
    @JacksonXmlElementWrapper(useWrapping = false)
    val rhymeSets: List<ShelvesRytmicWords> = emptyList()
)

@JacksonXmlRootElement(localName = "data")
data class ShelvesRounds (
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    override val data: List<ShelvesRound> = emptyList()
) : IModel<ShelvesRound>