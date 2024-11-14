package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightDifferScreen.imageSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.dataLayer.EyesightSearchRepo
import com.example.bakalarkaapp.dataLayer.SearchItem
import com.example.bakalarkaapp.presentationLayer.BaseViewModel
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightMemoryScreen.EyesightMemoryViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EyesightSearchUiState(
    val bgImageResource: String,
    val items: List<SearchItem>,
    val remaining: Int
)

class EyesightSearchViewModel(searchRepo: EyesightSearchRepo) : BaseViewModel() {
    private val rounds = searchRepo.data
    private var currentRound = rounds[roundIdx]
    private var _uiState = MutableStateFlow(
        EyesightSearchUiState(
            currentRound.background.value,
            currentRound.items,
            0
        )
    )
    val uiState = _uiState.asStateFlow()
    var itemsFound = 0

    init {
        count = rounds.size
    }

    fun onItemClick() {
        itemsFound++
        if (itemsFound == currentRound.items.size) {
            score++
            itemsFound = 0
            if (nextRound()) {
                updateData()
            }
        }
    }

    override fun updateData() {
        currentRound = rounds[roundIdx]
        _uiState.update { state ->
            state.copy(
                bgImageResource = currentRound.background.value,
                items = currentRound.items
            )
        }
    }

    override fun doRestart() {
        updateData()
    }


}

class EyesightSearchViewModelFactory(private val searchRepo: EyesightSearchRepo) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyesightSearchViewModel::class.java)) {
            return EyesightSearchViewModel(searchRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: EyesightSearchViewModel")
    }
}