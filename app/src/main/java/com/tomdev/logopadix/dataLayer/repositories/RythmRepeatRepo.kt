package com.tomdev.logopadix.dataLayer.repositories

import android.content.Context
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.IModel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class RythmRepeatRepo(ctx: Context) :
    ResourceMappedRepository<RythmRepeatData, String>(
        ctx,
        R.raw.rythm_repeat_data,
        RythmRepeatData::class.java
    )

@JacksonXmlRootElement(localName = "data")
data class RythmRepeatData(
    @JacksonXmlProperty(localName = "soundName")
    @JacksonXmlElementWrapper(useWrapping = false)
    override val data: List<String> = emptyList()
) : IModel<String>