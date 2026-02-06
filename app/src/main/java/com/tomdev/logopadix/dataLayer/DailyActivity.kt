package com.tomdev.logopadix.dataLayer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_activity")
data class DailyActivity (
    @PrimaryKey
    val date: String,
    val practiced: Boolean,
    val frozen: Boolean
)