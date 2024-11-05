package com.example.bakalarkaapp

import android.app.Application
import com.example.bakalarkaapp.dataLayer.EyesightComparisonRepo
import com.example.bakalarkaapp.dataLayer.EyesightDifferRepo
import com.example.bakalarkaapp.dataLayer.EyesightMemoryRepo
import com.example.bakalarkaapp.dataLayer.HearingFonematicRepo

class LogoApp: Application() {
    val eyesightDifferRepository by lazy { EyesightDifferRepo(this) }
    val eyesightComparisonRepository by lazy { EyesightComparisonRepo(this) }
    val eyesightMemoryRepository by lazy { EyesightMemoryRepo(this) }
    val hearingFonematicRepository by lazy { HearingFonematicRepo(this) }
}