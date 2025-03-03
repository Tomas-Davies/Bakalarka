package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.models.EyesightSynthData
import com.example.bakalarkaapp.dataLayer.models.EyesightSynthRound

class EyesightSynthesisRepo(ctx: Context):
    ResourceMappedRepository<EyesightSynthData, EyesightSynthRound>(
        ctx,
        R.raw.eyesight_synth_data,
        EyesightSynthData::class.java
    ) {
    override val data = mappedClass?.rounds ?: emptyList()
}