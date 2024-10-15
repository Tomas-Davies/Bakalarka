package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightComparisonScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarkaapp.dataLayer.ComparisonItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed class ScreenState{
    data object Running: ScreenState()
    data object Finished: ScreenState()
}
data class EyesightComparisonUiState (
    val imageId: String,
    val answer: Boolean
)

class EyesightComparisonViewModel(private val comparisonData: List<ComparisonItem>): ViewModel() {
    private var data = comparisonData
    private var idx = 0
    private var currentItem = data[0]
    private val _uiState = MutableStateFlow(EyesightComparisonUiState(currentItem.imageId, currentItem.isSameShape.toBoolean()))
    val uiState: StateFlow<EyesightComparisonUiState> = _uiState.asStateFlow()
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Running)
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    fun updateData(){
        indexInc()
        currentItem = data[idx]
        _uiState.update { currentState ->
            currentState.copy(
                imageId = currentItem.imageId,
                answer = currentItem.isSameShape.toBoolean()
            )
        }
    }

    fun restart(){
        _screenState.value = ScreenState.Running
    }

    private fun indexInc(){
        if (idx+1 < comparisonData.size){
            idx++
        } else {
            idx = 0
            data = comparisonData.shuffled()
            _screenState.value = ScreenState.Finished
        }
    }
}

class EyesightComparionViewModelFactory(private val comparisonData: List<ComparisonItem>): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(EyesightComparisonViewModel::class.java)){
            return EyesightComparisonViewModel(comparisonData) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: EyesightComparisonViewModel")
    }
}