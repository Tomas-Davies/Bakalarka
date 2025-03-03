package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.models.SpeechData
import com.example.bakalarkaapp.dataLayer.models.SpeechLetter
import com.example.bakalarkaapp.dataLayer.models.WordContent

class SpeechRepo(ctx: Context) :
    ResourceMappedRepository<SpeechData, SpeechLetter>(
        ctx,
        R.raw.speech_data,
        SpeechData::class.java
    ) {
    override val data = mappedClass?.letters ?: emptyList()

    fun getWords(letterLabel: String, posLabel: String): List<WordContent>? {
        val letter = data.find { letter -> letter.label == letterLabel }
        val pos = letter?.positions?.find { pos -> pos.label == posLabel }
        return pos?.words
    }
}