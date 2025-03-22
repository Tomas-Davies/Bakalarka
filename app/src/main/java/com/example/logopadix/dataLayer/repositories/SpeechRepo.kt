package com.example.logopadix.dataLayer.repositories

import android.content.Context
import com.example.logopadix.LogoApp
import com.example.logopadix.R
import com.example.logopadix.dataLayer.IModel
import com.example.logopadix.dataLayer.UserSentence
import com.example.logopadix.dataLayer.WordContent
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import kotlinx.coroutines.flow.Flow


class SpeechRepo(ctx: Context) :
    ResourceMappedRepository<SpeechData, SpeechLetter>(
        ctx,
        R.raw.speech_data,
        SpeechData::class.java
    ) {
        val app = ctx as LogoApp
        private val database = app.database
        private val dao = database.sentencesDao()

    /**
     * @param letterLabel A label of the letter, for example "L".
     * @param posLabel A label of position that groups the words, for example "L_L".
     * @return list of [WordContent]
     */
    fun getWords(letterLabel: String, posLabel: String): List<WordContent>? {
        val letter = data.find { letter -> letter.label == letterLabel }
        if (posLabel.isNotEmpty()){
            val pos = letter?.positions?.find { pos -> pos.label == posLabel }
            return pos?.words
        } else {
            return letter?.positions?.get(0)?.words ?: emptyList()
        }
    }

    fun getDefaultSentences(letterLabel: String): List<String>? {
        val letter = data.find { letter -> letter.label == letterLabel }
        return letter?.sentences
    }

    // Database calls
    fun getUserSentences(letter: String): Flow<List<UserSentence>> {
        return dao.getSentencesByLetter(letter)
    }

    suspend fun addUserSentence(sentence: UserSentence){
        dao.addSentence(sentence)
    }

    suspend fun deleteUserSentence(sentence: UserSentence){
        dao.deleteSentence(sentence)
    }

    suspend fun updateUserSentence(sentence: UserSentence){
        dao.updateSentence(sentence)
    }
}

data class LetterPosition(
    val label: String = "",
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "word")
    val words: List<WordContent> = emptyList()
)

data class SpeechLetter(
    val label: String = "",
    val positions: List<LetterPosition> = emptyList(),
    val sentences: List<String> = emptyList()
){
    val isPrimitive = positions.isNotEmpty() && positions[0].label == "NONE"
}

@JacksonXmlRootElement(localName = "letters")
data class SpeechData(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "letter")
    override val data: List<SpeechLetter> = emptyList()
) : IModel<SpeechLetter>