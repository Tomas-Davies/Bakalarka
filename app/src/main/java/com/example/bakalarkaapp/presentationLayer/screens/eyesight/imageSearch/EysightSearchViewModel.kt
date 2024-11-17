package com.example.bakalarkaapp.presentationLayer.screens.eyesight.imageSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.dataLayer.EyesightSearchRepo
import com.example.bakalarkaapp.dataLayer.SearchItem
import com.example.bakalarkaapp.presentationLayer.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EyesightSearchUiState(
    val bgImageResource: String,
    val items: List<SearchItem>
)

class EyesightSearchViewModel(searchRepo: EyesightSearchRepo) : BaseViewModel() {
    private val rounds = searchRepo.data
    private var currentRound = rounds[roundIdx]
    private var _uiState = MutableStateFlow(
        EyesightSearchUiState(
            bgImageResource = currentRound.background.value,
            items = currentRound.items
        )
    )
    val uiState = _uiState.asStateFlow()
    private var _foundAll = MutableStateFlow(false)
    val foundAll = _foundAll.asStateFlow()
    private var _itemsFound = MutableStateFlow(0)
    var itemsFound = _itemsFound.asStateFlow()


    init {
        count = rounds.size
    }

    fun onItemClick() {
        _itemsFound.value++
        if (itemsFound.value == currentRound.items.size) {
            viewModelScope.launch {
                _foundAll.emit(true)
                score++
                delay(2000)
                _foundAll.emit(false)
                delay(500)
                if (nextRound()) {
                    updateData()
                }
                _itemsFound.value = 0
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