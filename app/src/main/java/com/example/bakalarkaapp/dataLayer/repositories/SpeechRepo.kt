package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.models.SpeechData
import com.example.bakalarkaapp.dataLayer.models.SpeechLetter
import com.example.bakalarkaapp.dataLayer.models.WordEntry

class SpeechRepo(ctx: Context) :
    XmlRepository<SpeechData, SpeechLetter>(
        ctx,
        R.raw.speech_data,
        SpeechData::class.java
    ) {
    override val data: List<SpeechLetter> = mappedClass?.letters ?: SpeechData().letters

    fun getWords(letterLabel: String, posLabel: String): List<WordEntry>? {
        val letter = data.find { letter -> letter.label == letterLabel }
        val pos = letter?.positions?.find { pos -> pos.label == posLabel }
        return pos?.words
    }
}