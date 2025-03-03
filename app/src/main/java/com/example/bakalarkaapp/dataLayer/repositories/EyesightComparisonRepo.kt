package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.models.ComparisonData
import com.example.bakalarkaapp.dataLayer.models.ComparisonItem

class EyesightComparisonRepo(ctx: Context) :
    ResourceMappedRepository<ComparisonData, ComparisonItem>(
        ctx,
        R.raw.eyesight_comparison_data,
        ComparisonData::class.java
    ) {
    override val data = mappedClass?.items ?: emptyList()
}