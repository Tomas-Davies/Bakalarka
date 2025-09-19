package com.tomdev.logopadix.dataLayer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 *  Data Access Object for [UserSentence]  database entity.
 *  This object provides methods used to interact with data in the *user_sentences* table.
 */
@Dao
interface UserSentenceDAO {
    @Query("SELECT * FROM user_sentences WHERE letter LIKE :letter")
    fun getSentencesByLetter(letter: String): Flow<List<UserSentence>>
    @Insert
    suspend fun addSentence(sentence: UserSentence): Long
    @Update
    suspend fun updateSentence(sentence: UserSentence): Int
    @Delete
    suspend fun deleteSentence(sentence: UserSentence)
}