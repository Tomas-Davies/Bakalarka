package com.example.bakalarkaapp.utils.image

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap

fun getFitContentScaleInImage(imageWidth: Float, imageHeight: Float, content: ImageBitmap): Float {
    val widthScale = imageWidth / content.width
    val heightScale = imageHeight / content.height
    return minOf(widthScale, heightScale)
}

fun getContentOffsetInImage(contentSize: Size, imageWidth: Float, imageHeight: Float): Offset {
    val imageX = (imageWidth - contentSize.width) / 2
    val imageY = (imageHeight - contentSize.height) / 2
    return Offset(imageX, imageY)
}

fun getContentSizeInImage(bitmap: ImageBitmap, contentScale: Float): Size {
    val width = bitmap.width * contentScale
    val height = bitmap.height * contentScale
    return Size(width, height)
}