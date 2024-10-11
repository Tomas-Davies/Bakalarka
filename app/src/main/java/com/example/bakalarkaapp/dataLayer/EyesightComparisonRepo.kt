package com.example.bakalarkaapp.dataLayer

import android.content.Context
import com.example.bakalarkaapp.XmlUtils
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

class EyesightComparisonRepo(context: Context) {

//    private val mappedClass: cData = XmlUtils().
//        parseXmlData(context, "eyesight_comparison_data", cData::class.java)
//    val data = mappedClass.data

}

@JsonRootName("comparisonItem")
class ComparisonItem {
    @JsonProperty("imageId")
    val imageId: String = ""

    @JsonProperty("isSameShape")
    val isSameShape: String = ""
}

@JsonRootName("data")
class cData {
    @JsonProperty("comparisonItem")
    val data: List<ComparisonItem> = ArrayList()
}

