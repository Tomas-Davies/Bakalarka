package com.example.bakalarkaapp.dataLayer

import android.content.Context
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

class HearingFonematicRepo(val context: Context) {
//    private val resources = context.resources
//    private val mappedClass = XmlUtils().parseXmlData(context, "hearing_fonematic_data", hfData::class.java)
//    val data: List<Words> = mappedClass.data

}


@JsonRootName("words")
data class Words(
    @JsonProperty("word")
    val words: List<String> = ArrayList()
)

@JsonRootName("data")
data class FonematicData(
    @JsonProperty("words")
    val data: List<Words> = ArrayList()
)