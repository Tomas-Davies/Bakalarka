package com.example.bakalarkaapp.dataLayer

import android.content.Context
import com.example.bakalarkaapp.XmlUtils
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName


class EyesightComparisonRepo(context: Context) {
    private val mappedClass = XmlUtils().
    parseXmlData(context, "eyesight_comparison_data", ComparisonData::class.java)
    val data: List<ComparisonItem> = mappedClass.data
}

@JsonRootName("comparisonItem")
class ComparisonItem {
    @JsonProperty("imageId")
    val imageId: String = ""

    @JsonProperty("isSameShape")
    val isSameShape: String = ""
}

@JsonRootName("data")
class ComparisonData {
    @JsonProperty("comparisonItem")
    val data: List<ComparisonItem> = ArrayList()
}

