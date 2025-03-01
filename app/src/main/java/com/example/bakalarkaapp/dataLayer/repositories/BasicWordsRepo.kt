package com.example.bakalarkaapp.dataLayer.repositories

import android.content.Context
import com.example.bakalarkaapp.dataLayer.models.BasicWords
import com.example.bakalarkaapp.dataLayer.models.BasicWordsRound


class BasicWordsRepo(ctx: Context, resourceFileId: Int):
    XmlRepository<BasicWords, BasicWordsRound>(
        ctx,
        resourceFileId,
        BasicWords::class.java
    ) {
    override val data = mappedClass?.rounds ?: emptyList()
}
