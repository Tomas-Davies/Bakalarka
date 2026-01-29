package com.tomdev.logopadix.dataLayer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface StickerProgressDao {
    @Query("SELECT * FROM sticker_progress")
    suspend fun getAll(): List<StickerProgress>

    @Query("SELECT * FROM sticker_progress WHERE stickerId LIKE :stickerId")
    suspend fun getStickerProgress(stickerId: String): StickerProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sticker: StickerProgress)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stickers: List<StickerProgress>)

    @Update
    suspend fun update(sticker: StickerProgress)
}