package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.models.EyesightSynthData
import com.example.bakalarkaapp.dataLayer.models.EyesightSynthRound

class EyesightSynthesisRepo(ctx: Context):
    XmlRepository<EyesightSynthData, EyesightSynthRound>(
        ctx,
        R.raw.eyesight_synth_data,
        EyesightSynthData::class.java
    ) {
    override val data: List<EyesightSynthRound> = mappedClass?.rounds ?: EyesightSynthData().rounds
}