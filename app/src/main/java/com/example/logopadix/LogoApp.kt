package com.example.logopadix

import android.app.Application
import com.example.logopadix.dataLayer.AppDb
import com.example.logopadix.dataLayer.repositories.EyesightComparisonRepo
import com.example.logopadix.dataLayer.repositories.EyesightDifferRepo
import com.example.logopadix.dataLayer.repositories.EyesightSearchRepo
import com.example.logopadix.dataLayer.repositories.EyesightSynthesisRepo
import com.example.logopadix.dataLayer.repositories.BasicWordsRepo
import com.example.logopadix.dataLayer.repositories.HearingMemoryRepo
import com.example.logopadix.dataLayer.repositories.RythmRepeatRepo
import com.example.logopadix.dataLayer.repositories.RythmShelvesRepo
import com.example.logopadix.dataLayer.repositories.RythmSyllablesRepo
import com.example.logopadix.dataLayer.repositories.SpeechRepo
import com.example.logopadix.dataLayer.repositories.TalesRepo

class LogoApp: Application() {
    val database by lazy { AppDb.getDatabase(this) }
    val speechRepository by lazy { SpeechRepo(this) }
    val eyesightDifferRepository by lazy { EyesightDifferRepo(this) }
    val eyesightComparisonRepository by lazy { EyesightComparisonRepo(this) }
    val eyesightMemoryRepository by lazy { BasicWordsRepo(this, R.raw.eyesight_memory_data) }
    val eyesightSearchRepository by lazy { EyesightSearchRepo(this) }
    val eyesightSynthesisRepository by lazy { EyesightSynthesisRepo(this) }
    val hearingFonematicRepository by lazy { BasicWordsRepo(this, R.raw.hearing_fonematic_data) }
    val hearingMemoryRepository by lazy { HearingMemoryRepo(this) }
    val hearingSynthesisRepository by lazy { BasicWordsRepo(this, R.raw.hearing_synthesis_data) }
    val rythmSyllablesRepository by lazy { RythmSyllablesRepo(this) }
    val rythmShelvesRepository by lazy { RythmShelvesRepo(this) }
    val rythmRepeatRepository by lazy { RythmRepeatRepo(this) }
    val talesRepository by lazy { TalesRepo(this) }
}