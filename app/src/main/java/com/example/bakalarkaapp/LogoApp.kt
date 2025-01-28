package com.example.bakalarkaapp

import android.app.Application
import com.example.bakalarkaapp.dataLayer.repositories.EyesightComparisonRepo
import com.example.bakalarkaapp.dataLayer.repositories.EyesightDifferRepo
import com.example.bakalarkaapp.dataLayer.repositories.EyesightMemoryRepo
import com.example.bakalarkaapp.dataLayer.repositories.EyesightSearchRepo
import com.example.bakalarkaapp.dataLayer.repositories.HearingFonematicRepo

class LogoApp: Application() {
    val eyesightDifferRepository by lazy { EyesightDifferRepo(this) }
    val eyesightComparisonRepository by lazy { EyesightComparisonRepo(this) }
    val eyesightMemoryRepository by lazy { EyesightMemoryRepo(this) }
    val eyesightSearchRepository by lazy { EyesightSearchRepo(this) }
    val hearingFonematicRepository by lazy { HearingFonematicRepo(this) }
}