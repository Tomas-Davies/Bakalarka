package com.tomdev.logopadix

import android.app.Application
import com.tomdev.logopadix.dataLayer.AppDb
import com.tomdev.logopadix.dataLayer.DayStreakRepo
import com.tomdev.logopadix.dataLayer.repositories.AchievementRepo
import com.tomdev.logopadix.dataLayer.repositories.EyesightComparisonRepo
import com.tomdev.logopadix.dataLayer.repositories.EyesightDifferRepo
import com.tomdev.logopadix.dataLayer.repositories.EyesightSearchRepo
import com.tomdev.logopadix.dataLayer.repositories.EyesightSynthesisRepo
import com.tomdev.logopadix.dataLayer.repositories.BasicWordsRepo
import com.tomdev.logopadix.dataLayer.repositories.HearingMemoryRepo
import com.tomdev.logopadix.dataLayer.repositories.RythmRepeatRepo
import com.tomdev.logopadix.dataLayer.repositories.RythmShelvesRepo
import com.tomdev.logopadix.dataLayer.repositories.RythmSyllablesRepo
import com.tomdev.logopadix.dataLayer.repositories.SpeechRepo
import com.tomdev.logopadix.dataLayer.repositories.StickerRepository
import com.tomdev.logopadix.dataLayer.repositories.StickerDefinitionsRepository
import com.tomdev.logopadix.dataLayer.repositories.TalesRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


class LogoApp: Application() {
    val appScopeIO = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val database by lazy { AppDb.getDatabase(this) }
    val speechRepository by lazy { SpeechRepo(this) }
    val eyesightDifferRepository by lazy { EyesightDifferRepo(this) }
    val eyesightComparisonRepository by lazy { EyesightComparisonRepo(this) }
    val eyesightMemoryRepository by lazy { BasicWordsRepo(this,
        R.raw.eyesight_memory_data
    ) }
    val eyesightSearchRepository by lazy { EyesightSearchRepo(this) }
    val eyesightSynthesisRepository by lazy { EyesightSynthesisRepo(this) }
    val hearingFonematicRepository by lazy { BasicWordsRepo(this,
        R.raw.hearing_fonematic_data
    ) }
    val hearingMemoryRepository by lazy { HearingMemoryRepo(this) }
    val hearingSynthesisRepository by lazy { BasicWordsRepo(this,
        R.raw.hearing_synthesis_data
    ) }
    val hearingAssignRepository by lazy { BasicWordsRepo(this,
        R.raw.hearing_assign_data
        ) }
    val rythmSyllablesRepository by lazy { RythmSyllablesRepo(this) }
    val rythmShelvesRepository by lazy { RythmShelvesRepo(this) }
    val rythmRepeatRepository by lazy { RythmRepeatRepo(this) }
    val talesRepository by lazy { TalesRepo(this) }

    val achievementsRepo by lazy { AchievementRepo(this) }
    val stickerDefinitionsRepo by lazy { StickerDefinitionsRepository(this) }
    val stickerRepo by lazy { StickerRepository(this, appScopeIO) }
    val dailyActivityRepo by lazy { DayStreakRepo(database.dailyActivityDao(), appScopeIO) }

}