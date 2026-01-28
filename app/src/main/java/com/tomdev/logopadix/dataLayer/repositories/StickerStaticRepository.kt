package com.tomdev.logopadix.dataLayer.repositories

import android.content.Context
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.IData

class StickerStaticRepository(ctx: Context) :
    ResourceMappedRepository<StickerData, Sticker>(
        ctx = ctx,
        resourceId = R.raw.a_stickers,
        dataClass = StickerData::class.java
    )
{ }

class Sticker {
    val id: String = ""
    val imageName: String = ""
    val label: String = ""
    val limitCount: Int = 0
}

@JacksonXmlRootElement(localName = "data")
class StickerData : IData<Sticker> {
    @JacksonXmlProperty(localName = "sticker")
    @JacksonXmlElementWrapper(useWrapping = false)
    override val data: List<Sticker> = emptyList()
}
