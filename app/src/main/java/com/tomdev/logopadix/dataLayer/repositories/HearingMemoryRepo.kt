package com.tomdev.logopadix.dataLayer.repositories

import android.content.Context
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.IData
import com.tomdev.logopadix.dataLayer.WordContent
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.tomdev.logopadix.dataLayer.ILevel

class HearingMemoryRepo(ctx: Context):
    ResourceMappedRepository<HearingMemoryData, HearingMemoryRound>(
        ctx,
        R.raw.hearing_memory_data,
        HearingMemoryData::class.java
    )

/**
 * Represents a round of the Hearing Memory exercise.
 *
 * @property objects A list of [WordContent] items, which are used in the round.
 * @property toBePlayedCount A count of words that user has to memorize.
 */
class HearingMemoryRound : ILevel {
    @JacksonXmlProperty(localName = "roundContent")
    @JacksonXmlElementWrapper(useWrapping = false)
    val objects: List<WordContent> = emptyList()
    val toBePlayedCount: Int = 0
    @JacksonXmlProperty(isAttribute = true)
    override val difficulty: String = ""
}

data class HearingMemoryData(
    @JacksonXmlProperty(localName = "round")
    @JacksonXmlElementWrapper(useWrapping = false)
    override val data: List<HearingMemoryRound> = emptyList()
) : IData<HearingMemoryRound>