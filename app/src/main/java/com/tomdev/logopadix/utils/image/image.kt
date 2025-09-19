package com.tomdev.logopadix.utils.image

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap

/**
 * Calculates the scale factor used on content inside Image composable container with ContentScale.Fit applied.
 *
 * @param imageWidth The width of the container image in pixels.
 * @param imageHeight The height of the container image in pixels.
 * @param content The bitmap content to be scaled.
 * @return The scale factor applied to the fitting content.
 */
fun getFitContentScaleInImage(imageWidth: Float, imageHeight: Float, content: ImageBitmap): Float {
    val widthScale = imageWidth / content.width
    val heightScale = imageHeight / content.height
    return minOf(widthScale, heightScale)
}

/**
 * Calculates the offset used on content inside Image composable container with ContentScale.Fit applied.
 *
 * @param contentSize The size of the content after scaling.
 * @param imageWidth The width of the container image in pixels.
 * @param imageHeight The height of the container image in pixels.
 * @return An Offset of the content with coordinates relative to the Image composable container.
 */
fun getContentOffsetInImage(contentSize: Size, imageWidth: Float, imageHeight: Float): Offset {
    val imageX = (imageWidth - contentSize.width) / 2
    val imageY = (imageHeight - contentSize.height) / 2
    return Offset(imageX, imageY)
}

/**
 * Calculates the resulting size of scaled content inside Image composable container with ContentScale.Fit applied.
 *
 * @param bitmap The source bitmap.
 * @param contentScale The scale factor that was applied to the content inside Image composable container with ContentScale.Fit applied.
 * @return A Size of the scaled content.
 */
fun getContentSizeInImage(bitmap: ImageBitmap, contentScale: Float): Size {
    val width = bitmap.width * contentScale
    val height = bitmap.height * contentScale
    return Size(width, height)
}