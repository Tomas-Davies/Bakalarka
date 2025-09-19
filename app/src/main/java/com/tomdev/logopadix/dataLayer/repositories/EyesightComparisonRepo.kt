package com.tomdev.logopadix.dataLayer.repositories

import android.content.Context
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.IModel
import com.tomdev.logopadix.presentationLayer.screens.levels.IImageLevel
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

class EyesightComparisonRepo(ctx: Context) :
    ResourceMappedRepository<ComparisonData, ComparisonItem>(
        ctx,
        R.raw.eyesight_comparison_data,
        ComparisonData::class.java
    )

@JacksonXmlRootElement(localName = "data")
data class ComparisonData(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "comparisonItem")
    override val data: List<ComparisonItem> = emptyList()
) : IModel<ComparisonItem>

data class ComparisonItem(
    override val imageName: String = "",
    val isSameShape: Boolean = true
) : IImageLevel