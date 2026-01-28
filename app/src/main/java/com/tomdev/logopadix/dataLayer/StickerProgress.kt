package com.tomdev.logopadix.dataLayer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sticker_progress")
data class StickerProgress(
    @PrimaryKey
    val stickerId: String,
    val collectedPieces: Int,
    val isGoldenUnlocked: Boolean
)