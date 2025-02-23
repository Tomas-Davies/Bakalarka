package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.models.RythmSyllabData
import com.example.bakalarkaapp.dataLayer.models.RythmSyllabRound

class RythmSyllablesRepo(ctx: Context):
    XmlRepository<RythmSyllabData, RythmSyllabRound>(
        ctx,
        R.raw.rythm_syllabels_data,
        RythmSyllabData::class.java
    )
{
    override val data = mappedClass?.rounds ?: RythmSyllabData().rounds
}