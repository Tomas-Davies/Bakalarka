package com.example.bakalarkaapp.presentationLayer.screens.rythm.rythmShelvesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.dataLayer.models.ShelvesRound
import com.example.bakalarkaapp.dataLayer.models.WordContent
import com.example.bakalarkaapp.dataLayer.repositories.RythmShelvesRepo
import com.example.bakalarkaapp.viewModels.RoundsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class RythmShelvesUiState(
    val objects: List<WordContent>,
    val firstPart: List<WordContent>,
    val secondPart: List<WordContent>,
    val thirdPart: List<WordContent>
)

class RythmShelvesViewModel(
    private val repo: RythmShelvesRepo,
    app: LogoApp
) : RoundsViewModel(app) {

    private lateinit var rounds: List<ShelvesRound>
    private lateinit var currRound: ShelvesRound
    private lateinit var currRhymesSet: List<Pair<WordContent, WordContent>>
    private lateinit var allWords: List<WordContent>
    lateinit var parts: Triple<List<WordContent>, List<WordContent>, List<WordContent>>
    private lateinit var _uiState: MutableStateFlow<RythmShelvesUiState>
    lateinit var uiState: StateFlow<RythmShelvesUiState>
        private set

    private var rhymeCounter = 0
    private lateinit var _rhymePairsEnabled: MutableStateFlow<MutableList<Boolean>>
    lateinit var rhymePairsEnabled: StateFlow<List<Boolean>>
        private set

    private var _firstIdxClicked = MutableStateFlow(-1)
    val firstIdxClicked = _firstIdxClicked.asStateFlow()
    private var _secondIdxClicked = MutableStateFlow(-1)
    val secondIdxClicked = _secondIdxClicked.asStateFlow()
    private var pairCount = 0
    override var roundSetSize = 1

    init {
        viewModelScope.launch {
            repo.loadData()
            rounds = repo.data.shuffled()
            count = rounds.size
            currRound = rounds[roundIdx]
            currRhymesSet = getRhymeSet()
            allWords = getAllWords()
            parts = getThreeParts(allWords)
            _uiState = MutableStateFlow(
                RythmShelvesUiState(allWords, parts.first, parts.second, parts.third)
            )
            uiState = _uiState.asStateFlow()
            _rhymePairsEnabled = MutableStateFlow(MutableList(allWords.size) { true })
            rhymePairsEnabled = _rhymePairsEnabled.asStateFlow()
            disableButtons()
            dataLoaded()
        }
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
        if (didReset) disableButtons()

        if (!didReset){
            playSound(soundId)
            if (_firstIdxClicked.value == -1) _firstIdxClicked.update { idx }
            else _secondIdxClicked.update { idx }

            if (_firstIdxClicked.value != -1 && _secondIdxClicked.value != -1){
                enableButtons()
            }
        }
    }


    fun onDoneBtnClick(){
        clickedCounterInc()
        if (pairCorrect(_firstIdxClicked.value, _secondIdxClicked.value)){
            onMatchingPair()
            if (rhymeCounter == currRhymesSet.size) {
                onAllPairsDone()
            }
        } else {
            playOnWrongSound()
        }
        _firstIdxClicked.update { -1 }
        _secondIdxClicked.update { -1 }
        _buttonsEnabled.update { false }
    }


    private fun onMatchingPair(){
        playResultSound(true)
        _rhymePairsEnabled.value[_firstIdxClicked.value] = false
        _rhymePairsEnabled.value[_secondIdxClicked.value] = false
        rhymeCounter++
        scoreInc()
    }


    private fun onAllPairsDone(){
        viewModelScope.launch {
            showCorrectMessage()
            roundsCompletedInc()
            if(roundSetCompletedCheck()){
                showRoundSetDialog()
            } else {
                doContinue()
            }
        }
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


class RythmShelvesViewModelFactory(
    private val repo: RythmShelvesRepo,
    private val app: LogoApp
): ViewModelProvider.Factory
{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RythmShelvesViewModel::class.java)){
            return RythmShelvesViewModel(repo, app) as T
        }
        throw IllegalArgumentException("Unknown viewModel class: $modelClass")
    }
}