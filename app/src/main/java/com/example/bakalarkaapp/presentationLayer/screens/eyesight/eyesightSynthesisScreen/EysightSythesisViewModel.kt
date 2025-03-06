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
    val pieces: List<ImagePiece>
)

/**
 * Holds information about a piece that was cut from image.
 *
 * @property bitmap A bitmap piece from the original bitmap.
 * @property imageX An x-coordinate inside the original bitmap.
 * @property imageY An y-coordinate iniside the original bitmap.
 * @property width The width of the piece bitmap.
 * @property height The height of the piece bitmap.
 * @property initialOffset
 */
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
    private val levelIndex: Int,
    private val appContext: Context
) : RoundsViewModel(app) {
    init {
        roundIdx = levelIndex
    }

    private val rounds = app.eyesightSynthesisRepository.data
    private val bitmaps = imageNamesToBitmaps(rounds)
    private var currImage = bitmaps[roundIdx]
    private var pieceCount = rounds[roundIdx].pieceCount
    private val threshold = 200
    private var placedPieces = 0
    private var pieceSum = pieceCount
    private val _uiState =
        MutableStateFlow(EyesightSynthesisUiState(currImage, cutImage(currImage, pieceCount)))
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
            delay(1500)
            updateData()
        }
    }

    override fun updateData() {
        if (nextRound()) {
            currImage = bitmaps[roundIdx]
            pieceCount = rounds[roundIdx].pieceCount
            placedPieces = 0
            pieceSum += pieceCount
            updateState()
        }
    }

    private fun updateState() {
        _uiState.update { currentState ->
            currentState.copy(
                image = currImage,
                pieces = cutImage(currImage, pieceCount)
            )
        }
    }

    override fun doRestart() {
        roundIdx = levelIndex
        currImage = bitmaps[roundIdx]
        pieceCount = rounds[roundIdx].pieceCount
        placedPieces = 0
        pieceSum = pieceCount
        updateState()
    }

    override fun scorePercentage(): Int {
        return (score * 100) / pieceSum
    }


    //                                IMAGE PIECE LOGIC
    /**
     * This function will generate Rectangular pieces from given bitmap.
     *
     * @param image The image that is going to be cut to pieces.
     * @param pieceCount The count of pieces that are going to be cut out of the image.
     * @return a list of [pieces][ImagePiece], holding data about individual image pieces.
     */
    private fun cutImage(
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

    /**
     * This function generates list of [rectangles][Rect] representing image pieces by repeatedly splitting
     * the largest rectangle using the [splitRectangle] method.
     *
     * Splitting starts from rectangle that represents whole image, until the amount of
     * rectangles isn't equal to *pieceCount*.
     *
     * @param imageWidth The width of the *image* in pixels.
     * @param imageHeight The height of the *image* in pixels.
     * @param pieceCount The desired count of rectangles to generate.
     * @return A list of [rectangles][Rect] representing all image pieces.
     */
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
            rects.add(rect1)
            rects.add(rect2)
        }
        return rects
    }


    /**
     * Generates two [rectangles][Rect] from the given rectangle *rect*.
     *
     * Takes in account the size proportions of the given rectangle (vertical or horizontal split).
     *
     * @param rect The rectangle from which will the new rectangles originate.
     * @return A [pair][Pair] of two [rectangles][Rect] representing the split parts of the original rectangle.
     */
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


    /**
     * @param piece The piece which holds data about the original position.
     * @param currOffset The current offset of the piece composable.
     * @param contentOffset The offset of the content inside the Image composable.
     * @param contentScale The scale of the content inside the Image composable.
     * @param bottomBoxOffset The offset of the image piece container.
     * @return [Offset] which aligns the *image piece* into the original image.
     */
    fun setCorrectOffset(
        piece: ImagePiece,
        currOffset: Offset,
        contentOffset: Offset,
        contentScale: Float,
        bottomBoxOffset: Offset
    ): Offset {
        val inContentX = currOffset.x - contentOffset.x
        val inContentY = currOffset.y - contentOffset.y
        val scaledTargetX = piece.imageX * contentScale
        val scaledTargetY = piece.imageY * contentScale
        val dist = distance(scaledTargetX, scaledTargetY, inContentX, inContentY)

        if (dist <= threshold) {
            val offset = Offset(
                x = contentOffset.x + scaledTargetX,
                y = contentOffset.y + scaledTargetY
            )
            placedPieces++
            scoreInc()
            playResultSound(true)
            vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
            return offset - bottomBoxOffset
        } else {
            scoreDesc()
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

    fun onCorrectlyPlaced() {
        if (placedPieces == pieceCount) {
            moveNext()
        }
    }


    /**
     * Calculates initial offsets of image pieces to properly place them within a container.
     *
     * @param containerWidth The width of the container where the pieces will be placed.
     * @param containerHeight The height of the container where the pieces will be placed.
     * @param scaledPieces List of image pieces that have already been scaled appropriately.
     * @return A mutable list of [offsets][Offset] for each piece.
     */
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

    private fun findSquare(num: Int): Int {
        var sq = 1
        while (sq * sq < num) {
            sq++
        }
        return sq
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