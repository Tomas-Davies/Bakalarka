package com.example.bakalarkaapp.presentationLayer.screens.rythm.rythmShelvesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.dataLayer.models.WordContent
import com.example.bakalarkaapp.viewModels.RoundsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RythmShelvesUiState(
    val objects: List<WordContent>,
    val firstPart: List<WordContent>,
    val secondPart: List<WordContent>,
    val thirdPart: List<WordContent>
)

class RythmShelvesViewModel(app: LogoApp) : RoundsViewModel(app) {
    private val repo = app.rythmShelvesRepository
    private val rounds = repo.data.shuffled()
    private var currRound = rounds[roundIdx]
    private var currRhymesSet = getRhymeSet()
    private var allWords = getAllWords()
    var parts = getThreeParts(allWords)
    private val _uiState = MutableStateFlow(
        RythmShelvesUiState(allWords, parts.first, parts.second, parts.third)
    )
    val uiState = _uiState.asStateFlow()
    var rhymeCounter = 0
    private val _rhymePairsEnabled = MutableStateFlow(MutableList(allWords.size) { true })
    val rhymePairsEnabled = _rhymePairsEnabled.asStateFlow()
    private var _firstIdxClicked = MutableStateFlow(-1)
    val firstIdxClicked = _firstIdxClicked.asStateFlow()
    private var _secondIdxClicked = MutableStateFlow(-1)
    val secondIdxClicked = _secondIdxClicked.asStateFlow()
    private var pairCount = 0

    init {
        _buttonsEnabled.update { false }
        count = rounds.size
    }

    fun onCardClick(idx: Int, soundId: Int) {
        var didReset = false
        if (_firstIdxClicked.value == idx) {
            _firstIdxClicked.update { -1 }
            didReset = true
        }
        if (_secondIdxClicked.value == idx) {
            _secondIdxClicked.update { -1 }
            didReset = true
        }
        if (didReset) _buttonsEnabled.update { false }

        if (!didReset){
            playSound(soundId)
            if (_firstIdxClicked.value == -1) _firstIdxClicked.update { idx }
            else _secondIdxClicked.value = idx

            if (_firstIdxClicked.value != -1 && _secondIdxClicked.value != -1){
                _buttonsEnabled.update { true }
            }
        }
    }


    fun onDoneBtnClick(){
        if (pairCorrect(_firstIdxClicked.value, _secondIdxClicked.value)){
            playResultSound(true)
            _rhymePairsEnabled.value[_firstIdxClicked.value] = false
            _rhymePairsEnabled.value[_secondIdxClicked.value] = false
            rhymeCounter++
            scoreInc()
            if (rhymeCounter == currRhymesSet.size) {
                viewModelScope.launch {
                    showMessage(result = true)
                    delay(1500)
                    if (nextRound()) updateData()
                }
            }
        } else {
            scoreDesc()
            playResultSound(result = false)
        }
        _firstIdxClicked.update { -1 }
        _secondIdxClicked.update { -1 }
        _buttonsEnabled.update { false }
    }


    override fun updateData() {
        currRound = rounds[roundIdx]
        currRhymesSet = getRhymeSet()
        allWords = getAllWords()
        parts = getThreeParts(allWords)
        _uiState.update { state ->
            state.copy(
                objects = allWords,
                firstPart = parts.first,
                secondPart = parts.second,
                thirdPart = parts.third
            )
        }
        rhymeCounter = 0
        _rhymePairsEnabled.update { MutableList(allWords.size) { true } }
        _firstIdxClicked.value = -1
        _secondIdxClicked.value = -1
    }

    override fun scorePercentage(): Int {
        return (score * 100) / pairCount
    }

    override fun doRestart() {
        pairCount = 0
        updateData()
    }

    private fun getRhymeSet(): List<Pair<WordContent, WordContent>> {
        val l = currRound.rhymeSets.map { rythmSet ->
            val randRythm1 = rythmSet.rytmicWords.random()
            var randRythm2 = rythmSet.rytmicWords.random()

            while (randRythm2 == randRythm1) randRythm2 = rythmSet.rytmicWords.random()

            Pair(randRythm1, randRythm2)
        }
        pairCount += l.size
        return l
    }

    private fun getAllWords(): List<WordContent> {
        val words: MutableList<WordContent> = mutableListOf()
        currRhymesSet.forEach { rhymePair ->
            words.add(rhymePair.first)
            words.add(rhymePair.second)
        }

        return words
    }

    private fun pairCorrect(first: Int, second: Int): Boolean {
        val pair = currRhymesSet.find { item ->
            item.first == allWords[first] && item.second == allWords[second]
                            ||
            item.first == allWords[second] && item.second == allWords[first]
        }
        return pair != null
    }

    private fun getThreeParts(list: List<WordContent>): Triple<List<WordContent>, List<WordContent>, List<WordContent>> {
        val partLen = list.size / 3
        val remainder = list.size % 3
        val firstPartEndIdx = partLen + if (remainder > 0) 1 else 0
        val secondPartEndIdx = firstPartEndIdx + partLen + if (remainder > 1) 1 else 0

        val shuffledList = list.shuffled()
        val firstPart = shuffledList.subList(0, firstPartEndIdx)
        val secondPart = shuffledList.subList(firstPartEndIdx, secondPartEndIdx)
        val thirdPart = shuffledList.subList(secondPartEndIdx, list.size)


        return Triple(firstPart, secondPart, thirdPart)
    }
}


class RythmShelvesViewModelFactory(private val app: LogoApp): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RythmShelvesViewModel::class.java)){
            return RythmShelvesViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown viewModel class: $modelClass")
    }
}