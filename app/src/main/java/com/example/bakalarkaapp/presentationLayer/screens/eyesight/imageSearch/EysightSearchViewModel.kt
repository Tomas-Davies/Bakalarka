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
import com.example.bakalarkaapp.dataLayer.models.SearchItemOverlay
import com.example.bakalarkaapp.viewModels.IValidationAnswer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EyesightSearchUiState(
    val bgImageResource: String,
    val items: List<SearchItemOverlay>
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

    override fun validationCond(answer: IValidationAnswer?): Boolean {
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
        validateAnswer(null)
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

    data class OverlayInfo(val xInImage: Float, val yInImage: Float, val width: Float, val height: Float)

    fun getOverlayInfo(item: SearchItemOverlay, imgSize: Size, imgOffset: Offset): OverlayInfo {
        val x = (item.xPerc / 100f) * imgSize.width
        val y = (item.yPerc / 100f) * imgSize.height

        return OverlayInfo(
            xInImage = imgOffset.x + x,
            yInImage = imgOffset.y + y,
            width = (item.widthPerc / 100f) * imgSize.width,
            height = (item.heightPerc / 100f) * imgSize.height
        )
    }


    fun logClickPercInImage(
        clickOffset: Offset,
        imgOffset: Offset,
        imgContent: ImageBitmap,
        contentScale: Float
    ){
        val offsetInImg = clickOffset - imgOffset
        val width = imgContent.width * contentScale
        val height = imgContent.height * contentScale
        if (offsetInImg.x in 0f..width
            &&
            offsetInImg.y in 0f..height
        ) {
            val xPercentage =
                (100 * offsetInImg.x) / width
            val yPercentage =
                (100 * offsetInImg.y) / height

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