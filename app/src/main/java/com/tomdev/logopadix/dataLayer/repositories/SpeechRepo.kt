package com.tomdev.logopadix.dataLayer.repositories

import android.content.Context
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.IData
import com.tomdev.logopadix.dataLayer.UserSentence
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText
import com.tomdev.logopadix.utils.string.normalizedWithCh
import kotlinx.coroutines.flow.Flow
import kotlin.collections.emptyList


data class NormalizedWordContent(
    val imageName: String,
    val soundName: String,
    val text: String,
    val letters: List<String>
)

class SpeechRepo(ctx: Context) :
    ResourceMappedRepository<SpeechData, SpeechLetter>(
        ctx,
        R.raw.speech_data,
        SpeechData::class.java
    ) {
        val app = ctx as com.tomdev.logopadix.LogoApp
        private val database = app.database
        private val dao = database.sentencesDao()

    /**
     * @param letterLabel A label of the letter, for example "L".
     * @param posLabel A label of position that groups the words, for example "L_L".
     * @return list of [SpeechWordContent]
     */
    fun getWords(letterLabel: String, posLabel: String): List<SpeechWordContent>? {
        val letter: SpeechLetter = data.find { letter -> letter.label == letterLabel } ?: return emptyList()
        if (posLabel.isNotEmpty()){
            val pos = letter.positions.find { pos -> pos.label == posLabel }
            return pos?.words ?: emptyList()
        } else {
            return letter.positions.firstOrNull()?.words ?: emptyList()
        }
    }


    fun getProcessedWords(): List<NormalizedWordContent>?{
        val processed = mutableListOf<NormalizedWordContent>()
        val distinctIndication = mutableSetOf<String>()

        for (letter in data){
            for(pos in letter.positions){
                for (word in pos.words){
                    if (!distinctIndication.add(word.text ?: "")) continue

                    val str = word.text?.lowercase() ?: continue
                    val normalized = NormalizedWordContent(
                        word.imageName ?: "",
                        word.soundName ?: "",
                        word.text ?: "",
                        str.normalizedWithCh()
                    )
                    processed.add(normalized)
                }
            }
        }
        return processed
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


class SpeechWordContent {
    @JacksonXmlProperty(isAttribute = true)
    val imageName: String? = null
    @JacksonXmlProperty(isAttribute = true)
    val soundName: String? = null
    @JacksonXmlProperty(isAttribute = true)
    val subSoundName: String? = null
    @JacksonXmlProperty(isAttribute = true)
    val subText: String? = null
    @JacksonXmlText
    var text: String? = null
}

class LetterPosition {
    @JacksonXmlProperty(isAttribute = true)
    val doesntKnowLetter: Boolean = false
    val label: String = ""
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "word")
    val words: List<SpeechWordContent> = emptyList()
}

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
) : IData<SpeechLetter>