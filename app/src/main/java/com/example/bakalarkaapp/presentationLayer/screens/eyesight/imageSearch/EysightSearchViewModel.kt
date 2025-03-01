package com.example.bakalarkaapp.presentationLayer.screens.eyesight.imageSearch

import android.os.VibrationEffect
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.viewModels.ValidatableRoundViewModel
import com.example.bakalarkaapp.dataLayer.models.SearchItem
import com.example.bakalarkaapp.viewModels.IValidationAnswer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EyesightSearchUiState(
    val bgImageResource: String,
    val items: List<SearchItem>
)

class EyesightSearchViewModel(app: LogoApp, levelIndex: Int) : ValidatableRoundViewModel(app) {
    init {
        roundIdx = levelIndex
    }

    private val searchRepo = app.eyesightSearchRepository
    private val rounds = searchRepo.data
    private var currentRound = rounds[roundIdx]
    private var _uiState = MutableStateFlow(
        EyesightSearchUiState(
            bgImageResource = currentRound.imageName,
            items = currentRound.items
        )
    )
    val uiState = _uiState.asStateFlow()
    private var _itemsFound = MutableStateFlow(0)
    var itemsFound = _itemsFound.asStateFlow()
    private var clickCounter = 0
    private var foundCatsCounter = 0
    private var _missIndicatorOffset = MutableStateFlow(Offset(0f,0f))
    var missIndicatorOffset = _missIndicatorOffset.asStateFlow()
    private var _showMissIndicator = MutableStateFlow(false)
    val showMissIndicator = _showMissIndicator.asStateFlow()


    init {
        count = rounds.size
    }

    override fun validationCond(answer: IValidationAnswer): Boolean {
        return itemsFound.value == currentRound.items.size
    }
    override fun playOnCorrectSound() {}
    override fun playOnWrongSound() {}
    override fun scoreDesc() {}
    override fun messageShowWrong() {}
    override fun afterNewData() {
        _itemsFound.update { 0 }
    }

    fun onItemClick() {
        playResultSound(true)
        vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
        clickCounter++
        foundCatsCounter++
        _itemsFound.value++
        validateAnswer(IValidationAnswer.BlankAnswer)
    }

    fun missClick(){
        clickCounter++
    }

    override fun scorePercentage(): Int {
        return (foundCatsCounter * 100) / clickCounter
    }

    override fun updateData() {
        currentRound = rounds[roundIdx]
        _uiState.update { state ->
            state.copy(
                bgImageResource = currentRound.imageName,
                items = currentRound.items
            )
        }
    }

    override fun doRestart() {
        updateData()
    }

    fun moveMissIndicator(offset: Offset){
        viewModelScope.launch {
            _missIndicatorOffset.update { offset }
            _showMissIndicator.update { true }
            delay(500)
            _showMissIndicator.update { false }
        }
    }

    data class OverlayStats(val xInImage: Float, val yInImage: Float, val width: Float, val height: Float)
    fun imgPercToSize(item: SearchItem, imgSize: Size, imgOffset: Offset): OverlayStats {
        val x = (item.xPerc / 100f) * imgSize.width
        val y = (item.yPerc / 100f) * imgSize.height
        val overlayStats = OverlayStats(
            xInImage = imgOffset.x + x,
            yInImage = imgOffset.y + y,
            width = (item.widthPerc / 100f) * imgSize.width,
            height = (item.heightPerc / 100f) * imgSize.height
        )
        return overlayStats
    }

    fun logClickPercInImage(
        clickOffset: Offset,
        imgOffset: Offset,
        imgContent: ImageBitmap,
        contentScale: Float
    ){
        val offsetInImage = clickOffset - imgOffset
        val width = imgContent.width * contentScale
        val height = imgContent.height * contentScale
        if (offsetInImage.x in 0f..width
            &&
            offsetInImage.y in 0f..height
        ) {
            val xPercentage =
                (100 * offsetInImage.x) / width
            val yPercentage =
                (100 * offsetInImage.y) / height

            Log.w(
                "SEARCH_IMAGE_PERC_CORDS",
                "\n\n\nx = $xPercentage%, y = $yPercentage%\n\n\n"
            )
        }
    }
}

class EyesightSearchViewModelFactory(private val app: LogoApp, private val levelIndex: Int) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyesightSearchViewModel::class.java)) {
            return EyesightSearchViewModel(app, levelIndex) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}