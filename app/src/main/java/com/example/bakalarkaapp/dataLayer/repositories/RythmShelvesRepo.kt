package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.models.ShelvesRound
import com.example.bakalarkaapp.dataLayer.models.ShelvesRounds

class RythmShelvesRepo(ctx: Context) :
    ResourceMappedRepository<ShelvesRounds, ShelvesRound>(
        ctx,
        R.raw.rythm_shelves_data,
        ShelvesRounds::class.java
    )
{
    override val data = mappedClass?.rounds ?: emptyList()
}