package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.models.DifferData
import com.example.bakalarkaapp.dataLayer.models.DifferItem

class EyesightDifferRepo(ctx: Context) :
    XmlRepository<DifferData, DifferItem>(
        ctx,
        R.raw.eyesight_differ_data,
        DifferData::class.java
    ) {
    override val data = mappedClass?.data ?: DifferData().data
}