package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.models.HearingMemoryData
import com.example.bakalarkaapp.dataLayer.models.HearingMemoryRound

class HearingMemoryRepo(ctx: Context):
    XmlRepository<HearingMemoryData, HearingMemoryRound>(
        ctx,
        R.raw.hearing_memory_data,
        HearingMemoryData::class.java
    ) {
    override val data = mappedClass?.rounds ?: emptyList()
}