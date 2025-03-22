package com.example.logopadix.dataLayer


import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Defines an SQLite entity representing simple user written sentences for a specific letter.
 * Each instance represents a row in *user_sentences* table.
 */
@Entity(tableName = "user_sentences")
data class UserSentence (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val letter: String,
    val sentence: String
)