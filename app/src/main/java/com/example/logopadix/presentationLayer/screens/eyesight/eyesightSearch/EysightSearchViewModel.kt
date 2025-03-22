package com.example.logopadix.presentationLayer.screens.eyesight.eyesightSearch

import android.os.VibrationEffect
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.logopadix.LogoApp
import com.example.logopadix.dataLayer.repositories.SearchItemOverlay
import com.example.logopadix.dataLayer.repositories.SearchRound
import com.example.logopadix.dataLayer.repositories.EyesightSearchRepo
import com.example.logopadix.presentationLayer.states.ScreenState
import com.example.logopadix.viewModels.RoundsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EyesightSearchUiState(
    val bgImageResource: String,
    val nonColorBgImageResource: String,
    val items: List<SearchItemOverlay>
)

class EyesightSearchViewModel(
    repo: EyesightSearchRepo,
    app: LogoApp,
    levelIndex: Int
): RoundsViewModel(app)
{
    lateinit var rounds: List<SearchRound>
    private lateinit var currentRound: SearchRound
    private lateinit var _uiState: MutableStateFlow<EyesightSearchUiState>
    lateinit var uiState: StateFlow<EyesightSearchUiState>
        private set

    private var _itemsFound = MutableStateFlow(0)
    var itemsFound = _itemsFound.asStateFlow()
    private var _missIndicatorOffset = MutableStateFlow(Offset(0f,0f))
    var missIndicatorOffset = _missIndicatorOffset.asStateFlow()
    private var _showMissIndicator = MutableStateFlow(false)
    val showMissIndicator = _showMissIndicator.asStateFlow()

    init {
        roundIdx = levelIndex
        viewModelScope.launch {
            repo.loadData()
            rounds = repo.data
            count = rounds.size
            currentRound = rounds[roundIdx]
            _uiState = MutableStateFlow(
                EyesightSearchUiState(
                    bgImageResource = currentRound.coloredImageName,
                    nonColorBgImageResource = currentRound.imageName,
                    items = currentRound.items
                )
            )
            uiState = _uiState
            _screenState.value = ScreenState.Success
        }
    }


    fun onOverlayClick() {
        playResultSound(true)
        vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
        clickCounter++
        scoreInc()
        _itemsFound.update { _itemsFound.value + 1 }
        if (itemsFound.value == currentRound.items.size) {
            roundsCompletedInc()
            if(roundSetCompletedCheck()) {
                showRoundSetDialog()
            } else {
                doContinue()
            }
        }
    }

    fun missClick(){
        clickCounter++
    }

    override fun scorePercentage(): Int {
        return (score * 100) / clickCounter
    }

    override var roundSetSize = 1

    override fun updateData() {
        clickCounter = 0
        currentRound = rounds[roundIdx]
        _uiState.update { state ->
            state.copy(
                bgImageResource = currentRound.coloredImageName,
                nonColorBgImageResource = currentRound.imageName,
                items = currentRound.items
            )
        }
        _itemsFound.update { 0 }
    }


    fun moveMissIndicator(offset: Offset){
        viewModelScope.launch {
            _missIndicatorOffset.update { offset }
            _showMissIndicator.update { true }
            delay(500)
            _showMissIndicator.update { false }
        }
    }

    data class OverlayInfo(
        val xInImage: Float,
        val yInImage: Float,
        val width: Float,
        val height: Float
    )

    /**
     * Calculates the overlay positioning and size based on its percentage values relative to the image.
     *
     * @param overlay The overlay item containing percentage-based position and size.
     * @param imageContentSize The size of the image on which the overlay will be placed.
     * @param imageContentOffset The offset of the image's position on the screen.
     * @return [OverlayInfo] containing the absolute positioning and size data of the overlay.
     */
    fun getOverlayInfo(overlay: SearchItemOverlay, imageContentSize: Size, imageContentOffset: Offset): OverlayInfo {
        val x = (overlay.xPerc / 100f) * imageContentSize.width
        val y = (overlay.yPerc / 100f) * imageContentSize.height

        return OverlayInfo(
            xInImage = imageContentOffset.x + x,
            yInImage = imageContentOffset.y + y,
            width = (overlay.widthPerc / 100f) * imageContentSize.width,
            height = (overlay.heightPerc / 100f) * imageContentSize.height
        )
    }

    /**
     * This function is used for logging the percentage values of user clicks inside the Image.
     * It is used only as a tool when developing new search levels.
     */
    fun logClickPercInImage(
        clickOffset: Offset,
        imageContentOffset: Offset,
        imageContent: ImageBitmap,
        contentScale: Float
    ){
        val offsetInImg = clickOffset - imageContentOffset
        val width = imageContent.width * contentScale
        val height = imageContent.height * contentScale
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

class EyesightSearchViewModelFactory(
    private val repo: EyesightSearchRepo,
    private val app: LogoApp,
    private val levelIndex: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyesightSearchViewModel::class.java)) {
            return EyesightSearchViewModel(repo, app, levelIndex) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}