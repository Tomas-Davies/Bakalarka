package com.tomdev.logopadix.dataLayer.repositories

import android.content.Context
import android.util.Log
import com.tomdev.logopadix.LogoApp

class StickerRepository(ctx: Context) {
    private val app = ctx as LogoApp
    private val defsRepo = app.stickerDefinitionsRepo
    private val progressDao = app.database.stickersDao()

    suspend fun getStickers(): List<StickerUiModel> {
        val progress = progressDao.getAll()
        val progressMap = progress.associateBy { it.stickerId }
        val stickerDefs = defsRepo.loadData()
        Log.w("XML LOADED STICKERS", "xml sticker count: ${stickerDefs.size}")
        return stickerDefs
            .map { def ->
                val progress = progressMap[def.id]

                StickerUiModel(
                    id = def.id,
                    imageName = def.imageName,
                    label = def.label,
                    pieceLimit = def.limitCount,
                    collectedPieceCount = progress?.collectedPieces ?: 0,
                    isGoldenUnlocked = progress?.isGoldenUnlocked ?: false
                )
            }
    }



}

data class StickerUiModel(
    val id: String,
    val imageName: String,
    val label: String,
    val pieceLimit: Int,
    val collectedPieceCount: Int,
    val isGoldenUnlocked: Boolean
)