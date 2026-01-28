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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sticker: StickerProgress)

    @Update
    suspend fun update(sticker: StickerProgress)
}