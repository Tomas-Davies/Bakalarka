package com.tomdev.logopadix.dataLayer.repositories

import android.content.Context
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.IData

class AchievementRepo(
    ctx: Context
): ResourceMappedRepository<Achievements, Achievement>(
    ctx = ctx,
    resourceId = R.raw.achievements,
    dataClass = Achievements::class.java
)

@JacksonXmlRootElement(localName = "data")
data class Achievements(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "achievement")
    override val data: List<Achievement>
) : IData<Achievement>


@JacksonXmlRootElement(localName = "achievement")
data class Achievement(
    var imageName: String,
    var label: String,
    var costPoints: Int
)