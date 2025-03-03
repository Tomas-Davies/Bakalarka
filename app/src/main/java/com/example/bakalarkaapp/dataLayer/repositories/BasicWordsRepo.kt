package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.dataLayer.models.BasicWordsRounds
import com.example.bakalarkaapp.dataLayer.models.BasicWordsRound


class BasicWordsRepo(ctx: Context, resourceFileId: Int):
    ResourceMappedRepository<BasicWordsRounds, BasicWordsRound>(
        ctx,
        resourceFileId,
        BasicWordsRounds::class.java
    ) {
    override val data = mappedClass?.rounds ?: emptyList()
}
