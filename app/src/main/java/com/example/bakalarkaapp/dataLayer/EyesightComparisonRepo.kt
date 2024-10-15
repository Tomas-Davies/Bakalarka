package com.example.bakalarkaapp.dataLayer

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName


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

