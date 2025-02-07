package com.example.bakalarkaapp

import android.app.Application
import com.example.bakalarkaapp.dataLayer.repositories.EyesightComparisonRepo
import com.example.bakalarkaapp.dataLayer.repositories.EyesightDifferRepo
import com.example.bakalarkaapp.dataLayer.repositories.EyesightMemoryRepo
import com.example.bakalarkaapp.dataLayer.repositories.EyesightSearchRepo
import com.example.bakalarkaapp.dataLayer.repositories.EyesightSynthesisRepo
import com.example.bakalarkaapp.dataLayer.repositories.HearingFonematicRepo
import com.example.bakalarkaapp.dataLayer.repositories.HearingMemoryRepo
import com.example.bakalarkaapp.dataLayer.repositories.HearingSynthesisRepo

class LogoApp: Application() {
    val eyesightDifferRepository by lazy { EyesightDifferRepo(this) }
    val eyesightComparisonRepository by lazy { EyesightComparisonRepo(this) }
    val eyesightMemoryRepository by lazy { EyesightMemoryRepo(this) }
    val eyesightSearchRepository by lazy { EyesightSearchRepo(this) }
    val eyesightSynthesisRepository by lazy { EyesightSynthesisRepo(this) }
    val hearingFonematicRepository by lazy { HearingFonematicRepo(this) }
    val hearingMemoryRepository by lazy { HearingMemoryRepo(this) }
    val hearingSynthesisRepository by lazy { HearingSynthesisRepo(this) }
}