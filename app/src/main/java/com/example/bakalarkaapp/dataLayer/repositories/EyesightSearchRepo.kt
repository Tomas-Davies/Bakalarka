package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.models.SearchData
import com.example.bakalarkaapp.dataLayer.models.SearchRound

class EyesightSearchRepo(ctx: Context):
    ResourceMappedRepository<SearchData, SearchRound>(
        ctx,
        R.raw.eyesight_search_data,
        SearchData::class.java
    ) {
    override val data = mappedClass?.rounds ?: emptyList()
}