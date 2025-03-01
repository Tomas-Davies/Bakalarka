package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightSynthesisScreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.VibrationEffect
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.models.EyesightSynthRound
import com.example.bakalarkaapp.viewModels.RoundsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random


data class EyesightSynthesisUiState(
    val image: Bitmap,
    val pieceCount: Int = 9
)

data class ImagePiece(
    val bitmap: Bitmap,
    var imageX: Int,
    var imageY: Int,
    var width: Int,
    var height: Int,
    var initialOffset: Offset = Offset.Zero
)

data class Rect(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
)

class EyesightSynthesisViewModel(
    app: LogoApp,
    levelIndex: Int,
    private val appContext: Context
) : RoundsViewModel(app) {
    init {
        roundIdx = levelIndex
    }

    private val rounds = app.eyesightSynthesisRepository.data
    private val bitmaps = imageNamesToBitmaps(rounds)
    private var currImage = bitmaps[roundIdx]
    private var pieceCount = rounds[roundIdx].pieceCount
    private var pieceMinSize = 100
    private val threshold = 200
    private var placedPieces = 0
    private val _uiState =
        MutableStateFlow(EyesightSynthesisUiState(currImage, pieceCount))
    val uiState = _uiState.asStateFlow()

    init {
        count = rounds.size
    }

    private fun moveNext() {
        viewModelScope.launch {
            showMessage(
                result = true,
                message = appContext.resources.getString(R.string.message_positive)
            )
            scoreInc()
            delay(1500)
            updateData()
        }
    }

    override fun updateData() {
        if (nextRound()) {
            currImage = bitmaps[roundIdx]
            pieceCount = rounds[roundIdx].pieceCount
            placedPieces = 0
            updateState()
        }
    }

    private fun updateState() {
        _uiState.update { currentState ->
            currentState.copy(
                image = currImage,
                pieceCount = pieceCount
            )
        }
    }


    //                                PUZZLE LOGIKA

    fun cutImage(
        image: Bitmap,
        pieceCount: Int
    ): List<ImagePiece> {
        val rects = generateRects(image.width, image.height, pieceCount)

        val pieces = rects.map { rect ->
            val pieceBitmap = Bitmap.createBitmap(
                image,
                rect.x,
                rect.y,
                rect.width,
                rect.height
            )

            ImagePiece(
                bitmap = pieceBitmap,
                imageX = rect.x,
                imageY = rect.y,
                width = rect.width,
                height = rect.height
            )
        }

        return pieces
    }


    private fun generateRects(
        imageWidth: Int,
        imageHeight: Int,
        pieceCount: Int
    ): List<Rect> {
        val rects = mutableListOf(Rect(0, 0, imageWidth, imageHeight))

        while (rects.size < pieceCount) {
            val largestPiece = rects.maxByOrNull { piece -> piece.width * piece.height } ?: break
            rects.remove(largestPiece)
            val (rect1, rect2) = splitRectangle(largestPiece)

            if (rect1.width >= pieceMinSize && rect1.height >= pieceMinSize) rects.add(rect1)
            if (rect2.width >= pieceMinSize && rect2.height >= pieceMinSize) rects.add(rect2)
        }
        return rects
    }


    private fun splitRectangle(rect: Rect): Pair<Rect, Rect> {
        val splitVerticaly: Boolean
        if (rect.width == rect.height) splitVerticaly = Random.nextBoolean()
        else splitVerticaly = rect.width > rect.height
        val splitRatio = Random.nextDouble(0.3, 0.7)

        if (splitVerticaly) {
            val splitAt = (rect.width * splitRatio).toInt()
            return Pair(
                Rect(rect.x, rect.y, splitAt, rect.height),
                Rect(rect.x + splitAt, rect.y, rect.width - splitAt, rect.height)
            )
        } else {
            val splitAt = (rect.height * splitRatio).toInt()
            return Pair(
                Rect(rect.x, rect.y, rect.width, splitAt),
                Rect(rect.x, rect.y + splitAt, rect.width, rect.height - splitAt)
            )
        }
    }

    fun setCorrectOffset(
        piece: ImagePiece,
        currOffset: Offset,
        contentOffset: Offset,
        contentScale: Float,
        bottomBoxOffset: Offset
    ): Offset {
        val inContentX = currOffset.x - contentOffset.x
        val inContentY = (bottomBoxOffset.y + currOffset.y) - contentOffset.y
        val scaledTargetX = piece.imageX * contentScale
        val scaledTargetY = piece.imageY * contentScale
        val dist = distance(scaledTargetX, scaledTargetY, inContentX, inContentY)

        if (dist <= threshold) {
            val offset = Offset(
                x = (scaledTargetX + contentOffset.x) - bottomBoxOffset.x,
                y = (scaledTargetY + contentOffset.y) - bottomBoxOffset.y
            )
            placedPieces++
            playResultSound(true)
            vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
            return offset
        } else {
            playResultSound(result = false)
            return piece.initialOffset
        }
    }

    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val xDist = abs(x1 - x2)
        val yDist = abs(y1 - y2)
        val dist = sqrt(xDist.pow(2) + yDist.pow(2))
        return dist
    }

    fun onAllPiecesPlaced() {
        if (placedPieces == pieceCount) {
            moveNext()
        }
    }

    fun getInitialOffsets(
        containerWidth: Float,
        containerHeight: Float,
        scaledPieces: List<ImagePiece>
    ): MutableList<Offset> {
        val offsets = mutableListOf<Offset>()
        val colCount = findSquare(scaledPieces.size)
        val rowCount = colCount
        val pieceWidth = containerWidth / colCount
        val pieceHeight = containerHeight / rowCount
        for (row in 0 until rowCount) {
            for (col in 0 until colCount) {
                val index = row * colCount + col
                if (index < scaledPieces.size) {
                    val x = col * pieceWidth
                    val y = row * pieceHeight
                    offsets.add(Offset(x, y))
                }
            }
        }
        return offsets
    }

    private fun imageNamesToBitmaps(rounds: List<EyesightSynthRound>): List<Bitmap> {
        val bitmaps = mutableListOf<Bitmap>()
        rounds.forEach { round ->
            val drawableId = getDrawableId(round.imageName)
            val options = BitmapFactory.Options()
            options.inScaled = false
            val bitmap = BitmapFactory.decodeResource(appContext.resources, drawableId, options)
            bitmaps.add(bitmap)
        }
        return bitmaps
    }

    private fun findSquare(num: Int): Int {
        var sq = 1
        while (sq * sq < num) {
            sq++
        }
        return sq
    }

}


class EyesightSynthesisViewModelFactory(
    private val app: LogoApp,
    private val levelIndex: Int,
    private val appContext: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EyesightSynthesisViewModel::class.java)) {
            return EyesightSynthesisViewModel(app, levelIndex, appContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}