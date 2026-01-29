package com.tomdev.logopadix.dataLayer.repositories

import android.content.Context
import com.tomdev.logopadix.LogoApp
import com.tomdev.logopadix.dataLayer.StickerProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class StickerRepository(ctx: Context, private val scope: CoroutineScope) {
    private val app = ctx as LogoApp
    private val defsRepo = app.stickerDefinitionsRepo
    private val progressDao = app.database.stickersDao()

    suspend fun getStickers(): List<StickerUiModel> {
        val progress = progressDao.getAll()
        val progressMap = progress.associateBy { it.stickerId }
        val stickerDefs = defsRepo.data.ifEmpty { defsRepo.loadData() }
        if (progress.isEmpty()){
            scope.launch {
                val stickers = stickerDefs.map { s -> StickerProgress(s.id, 0, false) }
                progressDao.insertAll(stickers)
            }
        }
        return stickerDefs
            .map { def ->
                val progress = progressMap[def.id]

                StickerUiModel(
                    id = def.id,
                    imageName = def.imageName,
                    label = def.label,
                    pieceLimit = def.limitCount,
                    def.description,
                    collectedPieceCount = progress?.collectedPieces ?: 0,
                    isGoldenUnlocked = progress?.isGoldenUnlocked ?: false
                )
            }
    }


    suspend fun collectedPiece(stickerId: String){
        val sticker = progressDao.getStickerProgress(stickerId)
        if (sticker != null) {
            sticker.collectedPieces++
            val stickerDefs = defsRepo.data.ifEmpty { defsRepo.loadData() }
            val limitCount = stickerDefs.first { it.id == stickerId }.limitCount
            sticker.isGoldenUnlocked = sticker.collectedPieces >= limitCount
            progressDao.update(sticker)
        }
    }


    suspend fun getStickerById(stickerId: String): StickerUiModel {
        val stickerProgressInfo = progressDao.getStickerProgress(stickerId)
        val stickerDefs = defsRepo.data.ifEmpty { defsRepo.loadData() }
        val stickerDef = stickerDefs.first { it.id == stickerId }

        val stickerModel = StickerUiModel(
            id = stickerDef.id,
            imageName = stickerDef.imageName,
            label = stickerDef.label,
            pieceLimit = stickerDef.limitCount,
            stickerDef.description,
            collectedPieceCount = stickerProgressInfo?.collectedPieces ?: 0,
            isGoldenUnlocked = stickerProgressInfo?.isGoldenUnlocked ?: false
        )

        return stickerModel
    }

}

data class StickerUiModel(
    val id: String,
    val imageName: String,
    val label: String,
    val pieceLimit: Int,
    val description: String,
    val collectedPieceCount: Int,
    val isGoldenUnlocked: Boolean
)


enum class StickerType(public val id: String){
    EYESIGHT_COMPARISON("sticker_1"),
    EYESIGHT_SEARCH(id = "sticker_2"),
    EYESIGHT_DIFFER(id = "sticker_3"),
    EYESIGHT_MEMORY(id = "sticker_4"),
    EYESIGHT_SYNTHESIS(id = "sticker_5"),

    HEARING_FONEMATIC(id = "sticker_6"),
    HEARING_MEMORY(id = "sticker_7"),
    HEARING_SYNTHESIS(id = "sticker_8"),
    HEARING_SOUNDS_DIFF(id = "sticker_9"),

    RYTHM_PAIRS(id = "sticker_10"),
    RYTHM_SYLLABLES(id = "sticker_11"),
    RYTHM_REPEAT(id = "sticker_12")
}