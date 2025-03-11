package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.models.RythmRepeatData

class RythmRepeatRepo(ctx: Context) :
    ResourceMappedRepository<RythmRepeatData, String>(
        ctx,
        R.raw.rythm_repeat_data,
        RythmRepeatData::class.java
    )